#include "mainwindow.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.setWindowTitle("C++源代码单词扫描程序（词法分析）");
    w.show();

    return a.exec();
}
