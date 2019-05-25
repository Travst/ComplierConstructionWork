#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QFileDialog>
#include <QTextStream>
#include <stdexcept>


namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    void on_chooseButton_clicked();

    void on_saveButton_clicked();

    void on_treeButton_clicked();

    void on_saveasButton_clicked();

private:
    Ui::MainWindow *ui;
    QString tinyfile;
    const QString treefile = "tree";
    bool flag_isopen;
};

#endif // MAINWINDOW_H
