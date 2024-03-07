package com.office.library.book.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;

@Service
public class BookService {
	final static public int BOOK_ISBN_ALREADY_EXIST=0; 	//등록된 도서
	final static public int BOOK_REGISTER_SUCCESS=1; 	//신규 도서 등록 성공
	final static public int BOOK_REGISTER_FAIL=-1; 		//신규 도서 등록 실패
	
	@Autowired
	BookDao bookDao;
	
	public int registerBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] registerBookConfirm()");
		
		boolean isISBN = bookDao.isISBN(bookVo.getB_isbn());
		
		if(!isISBN) {
			int result=bookDao.insertBook(bookVo);
			
			if(result>0) {
				return BOOK_REGISTER_SUCCESS;
			}
			else {
				return BOOK_REGISTER_FAIL;
			}
		}
		else {
			return BOOK_ISBN_ALREADY_EXIST;
		}
	}

	public List<BookVo> searchBookConfirm(BookVo bookVo) {
		System.out.println("[BookService] searchBookConfirm()");
		return bookDao.selectBooksBySearch(bookVo);
	}

	public BookVo bookDetail(int b_no) {
		System.out.println("[BookService] bookDetail()");
		return bookDao.selectBook(b_no);
	}

	public BookVo modifyBookForm(int b_no) {
		System.out.println("[BookService] modifyBookForm()");
		return bookDao.selectBook(b_no);
	}

	public int modifyBookForm(BookVo bookVo) {
		System.out.println("[BookService] modifyBookForm()");
		return bookDao.updateBook(bookVo);
	}

	public int deleteBookConfirm(int b_no) {
		System.out.println("[BookService] deleteBookConfirm()");
		return bookDao.deleteBook(b_no);
	}

	public List<RentalBookVo> getRentalBooks() {
		System.out.println("[BookService] getRentalBooks()");
		return bookDao.selectRentalBooks();
	}

	public int returnBookConfirm(int b_no, int rb_no) {
		System.out.println("[BookService] returnBookConfirm()");
		
		int result=bookDao.updateRentalBooks(rb_no);
		if(result>0) {
			result = bookDao.updateBook(b_no);
		}
		return result;
	}

	public List<HopeBookVo> getHopeBooks() {
		System.out.println("[BookService] getHopeBooks()");
		return bookDao.getHopeBooks();
	}

	public int registerHopeBookConfirm(BookVo bookVo, int hb_no) {
		System.out.println("[BookService] registerHopeBookConfirm()");
		
		boolean isISBN=bookDao.isISBN(bookVo.getB_isbn()); 	//isbn으로 이미 도서가 등록 되어 있는가 확인
		
		if(!isISBN) {										//등록 안 되어 있으면
			int result=bookDao.insertBook(bookVo);			//등록 해보고
			if(result>0) {									//등록 성골하면
				bookDao.updateHopeBookResult(hb_no);		//희망도서 DB 업데이트
				return BOOK_REGISTER_SUCCESS;
			}
			else {
				return BOOK_REGISTER_FAIL;
			}
		}
		else {
			return BOOK_ISBN_ALREADY_EXIST;
		}
	}

	public List<BookVo> getAllBooks() {
		System.out.println("[BookService] getAllBooks()");
		return bookDao.selectAllBooks();
	}
}
