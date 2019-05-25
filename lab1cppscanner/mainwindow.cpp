#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_choose_clicked()
{
    QString filepath = QFileDialog::getOpenFileName(this,"选择C++文件","","c++(*.cpp *.h)");
    QString abspath = QFileInfo(filepath).absoluteFilePath();
    //cout<<filepath.toStdString()<<"\n"<<abspath.toStdString();
    Scanner s;
    try {
        s.Scan(string(abspath.toLocal8Bit()));//QString转string排除中文乱码问题，QString->QByteArray->string
        QFile rfile(QString::fromStdString(s.resultFileName()));
        if(!rfile.open(QFile::ReadOnly | QFile::Text))
            throw "read result file faild\n";
        QTextStream in(&rfile);
        ui->textBrowser->setText(in.readAll());
    } catch (const char* e) {
        ui->textBrowser->setText(e);
    }
}
