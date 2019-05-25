#include "scanner.h"

void Scanner::Scan(string path,string fname)
{
    scanfile.open(path);
	if (!scanfile.is_open()) {
		throw "cannot open scanned file!\n";
		return;
	}
    filename = fname;
    outfile = ofstream(filename, ios::out);
	if (!scanfile.is_open()) {
		throw "cannot write  result file!\n";
		return;
	}

	while (!scanfile.eof()) {
		getline(scanfile, readline);
		line_lenth = readline.length();
        unsigned int i = 0;
		while (i < line_lenth) {
			check_char = readline[i];
			if (check_char == ' ' || check_char == '\t'){
				++i;
				continue;
			}//skip the blank
			else if(isdigit(check_char)){
				i = findNum(i);
			}
			else if (isalpha(check_char)) {
				i = findWord(i);
			}
            else if (check_char == '"') {
				i = findStr(i);
			}
            else if(check_char == '\''){
                i = findApos(i);
            }
			else {
				i = findSymbol(i);
			}
			++i;
		}
	}

	scanfile.close();
	outfile.close();
}

void Scanner::writeResult(string &res, resultType rt)
{
	switch (rt)
	{
	case ID:
		res += "		Identifier\n";
		break;
	case KEYWORD:
		res += "		KeyWord\n";
		break;
	case NUM:
		res += "		Number\n";
		break;
	case STR:
		res += "		String\n";
		break;
	case OPERATOR:
		res += "		Operator\n";
		break;
	case SYMBOLS:
		res += "		Specific Symbols\n";
		break;
	case ANNOTATION:
		res += "		Annotation\n";
		break;
    case ERROR://should not happen
    //default:
		res += "		Cannot Recogonize\n";
		break;
	}
	outfile << res;
	res.clear();
}

unsigned int Scanner::getThis(unsigned int i)
{
	word += check_char;
	i += 1;
	check_char = readline[i];
	return i;
}

unsigned int Scanner::getNext(unsigned int i)
{
	i += 1;
	check_char = readline[i];
	word += check_char;
	return i;
}

char Scanner::peekNext(unsigned int i)
{
	return readline[i + 1];
}

unsigned int Scanner::findWord(unsigned int i)
{
	i = getThis(i);
	while (true) {
        if (isalpha(check_char) || isdigit(check_char) || check_char == '_')
			i = getThis(i);
		else
			break;
	}
	if (isKeyword(word))
		writeResult(word, KEYWORD);
	else
		writeResult(word, ID);
	
	//word.clear();
	return i - 1;
}

unsigned int Scanner::findNum(unsigned int i)
{
	i = getThis(i);
	bool isnum = true;
	while (true) {
		if (isdigit(check_char))
			i = getThis(i);
		else if (check_char == '.' && isnum) {
			i = getThis(i);
			isnum = false;
		}//float
        else if (tolower(check_char) == 'e' && (peekNext(i) == '+' || peekNext(i) == '-' || isdigit(peekNext(i)))) {
            i = getThis(i);
			isnum = true;
		}//scientific notation
		else
			break;
	}
	writeResult(word, NUM);
	//word.clear();
	return i - 1;
}

unsigned int Scanner::findStr(unsigned int i)
{
	word += check_char;
	while (true)
	{
		i += 1;
		if (i >= line_lenth)
		{
			getline(scanfile, readline);
			line_lenth = readline.size();
			i = 0;
		}
		else
			check_char = readline[i];
		word += check_char;
        if (check_char == '"')
			break;
	}
	writeResult(word, STR);
	//word.clear();
	return i;
}

unsigned int Scanner::findApos(unsigned int i)
{
    word += check_char;
    writeResult(word, SYMBOLS);
    i += 1;
    check_char = readline[i];
    if (check_char == '\\') {
        word += check_char;
        i += 1;
        check_char = readline[i];
    }
    word += check_char;
    writeResult(word, STR);
    i += 1;
    check_char = readline[i];
    word += check_char;
    writeResult(word, SYMBOLS);
    return i;
}

unsigned int Scanner::findSymbol(unsigned int i)
{
	word += check_char;
	resultType rt = OPERATOR;
	switch (check_char) {
	case '+':
		if (peekNext(i) == '+' || peekNext(i) == '=')
			i = getNext(i);// ++, +=
		else if (isdigit(peekNext(i))) {
			i = findNum(i);
			return i - 1;
		}// signed number
		break;
	case '-':
		if (peekNext(i) == '-' || peekNext(i) == '=')
			i = getNext(i);// --, -=
		else if (peekNext(i) == '>') {
			i = getNext(i);
			rt = SYMBOLS;
		}// ->
		else if (isdigit(peekNext(i))) {
			i = findNum(i);
			return i - 1;
		}// signed number
		break;
	case '*':
		if (peekNext(i) == '=')
			i = getNext(i);// *=
		break;
	case '/':
		if (peekNext(i) == '=')
			i = getNext(i);// /=
		else if (peekNext(i) == '/') {
			while (i < line_lenth)
				i = getNext(i);
			rt = ANNOTATION;
		}// single line comment
		else if (peekNext(i) == '*') {
			i = getNext(i);
			while (true) {
				i += 1;
				if (i >= line_lenth)
				{
					getline(scanfile, readline);
					line_lenth = readline.size();
					check_char = readline[0];
					i = 0;
				}
				else if (check_char == '*' && readline[i] == '/') {
					word += readline[i];
					break;
				}
				else
					check_char = readline[i];
				word += check_char;
			}// (piece of shit)
			rt = ANNOTATION;
		}// muliti-line comment
		break;
	case '=':
	case '%':
	case '!':
		if (peekNext(i) == '=')
			i = getNext(i);// !=
		break;
	case '&':
		if (peekNext(i) == '=')
			i = getNext(i);// &=
		else if (peekNext(i) == '&'){
			i = getNext(i);
			rt = SYMBOLS;
		}// &&
		break;
	case '|':
		if (peekNext(i) == '=')
			i = getNext(i);// |=
		else if (peekNext(i) == '|') {
			i = getNext(i);
			rt = SYMBOLS;
		}// ||
		break;
	case '>':
		if (peekNext(i) == '=')
			i = getNext(i);// >=
		else if (peekNext(i) == '>') {
			i = getNext(i);// >>
			if (peekNext(i) == '=')
				i = getNext(i);// >>=
		}
		break;
	case '<':
		if (peekNext(i) == '=')
			i = getNext(i);// <=
		else if (peekNext(i) == '<') {
			i = getNext(i);// <<
			if (peekNext(i) == '=')
				i = getNext(i);// <<=
		}
		break;
	case '~':
	case '#':
	default:
		rt = SYMBOLS;
		break;
	}
	writeResult(word, rt);
	//word.clear();
	return i;
}

bool Scanner::isKeyword(const string & str)
{
	auto res = find(begin(keyword), end(keyword), str);
	if (res != end(keyword))
		return true;
	return false;
}
