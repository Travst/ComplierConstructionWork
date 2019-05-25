#pragma once
#include<iostream>
#include<fstream>
#include<string>
#include<vector>
#include<algorithm>
using namespace std;

//enum that identifies the type
enum resultType {
	ID, KEYWORD, NUM, STR, OPERATOR, SYMBOLS, ANNOTATION, ERROR
};

class Scanner {
private:
	vector<string> keyword{
		"asm", "auto","bool","break","case","catch","char","class","const",
		"const_cast","continue","default","delete","do","double","dynamic_cast",
		"else","enum","explicit","export","extern","false","float","for",
        "friend","goto","if","inline","int","long","mutable","namespace",
        "new","operator","private","protected","public","register","reinterpret_cast",
		"return","short","signed","sizeof","static","static_cast","struct",
		"switch","template","this","throw","ture","try","typeof","typeid",
		"typename","union","unsigned","using","virtual","void","volatile","wchar_t"
	};
	string filename;//file's name that write the results
	ifstream scanfile;//file to scan
	ofstream outfile;//output file
	string readline;// read one line from file
	string word;// temperate one result from each findings
	char check_char;// temperate one char from the readline
    unsigned int line_lenth;// readline's length

	bool isKeyword(const string &str);
    unsigned int getThis(unsigned int i);// get one char in readline[i], return i+1
    unsigned int getNext(unsigned int i);// get one char in readline[i+1], return i+1
    char peekNext(unsigned int i);// return readline[i+1]
	//find begin with i, return a serial number
    unsigned int findWord(unsigned int i);
    unsigned int findNum(unsigned int i);
    unsigned int findStr(unsigned int i);
    unsigned int findApos(unsigned int i);//check the apostrophe
    unsigned int findSymbol(unsigned int i);
	// write one found result -res-, type -rt- to file
	void writeResult(string &res, resultType rt);

public:
	//scan file from path, write result to fname
    void Scan(string path, string fname = "result.t");
    string resultFileName(){
        return filename;
    }
};
