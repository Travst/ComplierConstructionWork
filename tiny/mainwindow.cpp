#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "globals.h"
#include "parse.h"
#include "scan.h"
#include "util.h"

/* allocate global variables */
int lineno = 0;
FILE * source;
FILE * listing;
FILE * code;

/* allocate and set tracing flags */
int EchoSource = FALSE;
int TraceScan = FALSE;
int TraceParse = FALSE;
int TraceAnalyze = FALSE;
int TraceCode = FALSE;

int Error = FALSE;


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    flag_isopen = false;
    QFile file(treefile);
    file.open(QFile::WriteOnly);
    file.close();
}

MainWindow::~MainWindow()
{
    delete ui;
    QFile::remove(treefile);
}

void MainWindow::on_chooseButton_clicked()
{
    QString openfile = QFileDialog::getOpenFileName(this,"Open Tiny File",
                                                    QDir::currentPath(),"tiny(*.tny)"); 
    try {
        tinyfile = openfile;
        QFile rfile(tinyfile);
        if(!rfile.open(QFile::ReadOnly|QFile::Text))
            throw std::runtime_error("read file failed!");
        flag_isopen = true;
        QTextStream qin(&rfile);
        ui->tnyBrowser->setText(qin.readAll());
        rfile.close();
    } catch (std::exception e) {
        ui->tnyBrowser->setText(e.what());
        flag_isopen = false;
    }

}

void MainWindow::on_saveButton_clicked()
{
    if (!flag_isopen){
        on_saveasButton_clicked();
    }
    else{
        try {
            QFile wfile(tinyfile);
            if(!wfile.open(QFile::WriteOnly|QFile::Text))
                throw std::runtime_error("read file failed!");
            flag_isopen = true;
            QTextStream qout(&wfile);
            qout << ui->tnyBrowser->toPlainText();
            wfile.close();
        } catch (std::exception e) {
            ui->tnyBrowser->setText(e.what());
            flag_isopen = false;
        }
    }
}

void MainWindow::on_saveasButton_clicked()
{
    QString savefile = QFileDialog::getSaveFileName(this,"Save Tiny File",
                                                   QDir::currentPath(),"tiny(*.tny");
    tinyfile = savefile;
    flag_isopen = true;
    on_saveButton_clicked();
}

void MainWindow::on_treeButton_clicked()
{
    ui->treeBrowser->clear();
    if (!flag_isopen){
        ui->treeBrowser->setText("\n hasn't open a file,\n SyntaxTree cannot be built");
    }
    else{
        try {
            source = fopen(tinyfile.toLocal8Bit(),"r");
            listing = fopen(treefile.toLocal8Bit(),"w");
            TreeNode * syntaxTree = parse();
            fprintf(listing,"\nSyntax tree:\n");
            printTree(syntaxTree);
            fclose(source);
            fclose(listing);
            QFile file(treefile);
            file.open(QFile::ReadOnly);
            QTextStream stream(&file);
            ui->treeBrowser->setText(stream.readAll());
            file.close();
        } catch (std::exception e) {
            ui->treeBrowser->setText(e.what());
        }
    }
}
