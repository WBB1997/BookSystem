--���Admin�˺��Ƿ����
CREATE OR REPLACE FUNCTION Check_Admin_Account(Admin_No IN Admin.No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Admin WHERE Admin_No = Admin.No;
  RETURN flag;
END Check_Admin_Account;

--�˶�Admin�˺�����
CREATE OR REPLACE FUNCTION Check_Admin(Admin_No       IN Admin.No%type,
                                       Admin_PassWord IN Admin.Password%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Admin
   WHERE Admin.No = Admin_No
     And Admin.Password = Admin_PassWord;
  RETURN flag;
END Check_Admin;

--���Staff�˺��Ƿ����
CREATE OR REPLACE FUNCTION Check_Staff_Account(Staff_No IN Staff.No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Staff WHERE Staff_No = Staff.No;
  RETURN flag;
END Check_Staff_Account;

--�˶�Staff�˺�����
CREATE OR REPLACE FUNCTION Check_Staff(Staff_No       IN Staff.No%type,
                                       Staff_PassWord IN Staff.Password%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Staff
   WHERE Staff.No = Staff_No
     And Staff.Password = Staff_PassWord;
  RETURN flag;
END Check_Staff;

--���Reader�˺��Ƿ����
CREATE OR REPLACE FUNCTION Check_Reader_Account(Reader_No IN Reader_Account.No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Reader_Account
   WHERE Reader_No = Reader_Account.No;
  RETURN flag;
END Check_Reader_Account;

--�˶�Reader�˺�����
CREATE OR REPLACE FUNCTION Check_Reader(Reader_No       IN Reader_Account.No%type,
                                        Reader_PassWord IN Reader_Account.Password%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Reader_Account
   WHERE Reader_Account.No = Reader_No
     And Reader_Account.Password = Reader_PassWord;
  RETURN flag;
END Check_Reader;

-- ����Reader����
CREATE OR REPLACE PROCEDURE default_Reader_Password(Reader_No      IN Reader_Account.No%type,
                                                    Admin_No       IN Admin.No%type,
                                                    Admin_Password IN Admin.Password%type) AS
  Account_Not_Exist EXCEPTION;
  myExp             EXCEPTION;
BEGIN
  IF check_reader_account(Reader_No) = 0 THEN
    raise Account_Not_Exist;
  ELSIF check_admin(Admin_No, Admin_Password) = 0 THEN
    raise myExp;
  ELSE
    UPDATE Reader_Account Set Password = Admin_No WHERE No = Reader_No;
  END IF;
EXCEPTION
  WHEN Account_Not_Exist THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ų����ڣ���ˢ�º�����$');
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20002, '$����Ա�˺��������$');
END default_Reader_Password;

-- ����Staff����
CREATE OR REPLACE PROCEDURE default_Staff_Password(Staff_No      IN Staff.No%type,
                                                    Admin_No       IN Admin.No%type,
                                                    Admin_Password IN Admin.Password%type) AS
  Account_Not_Exist EXCEPTION;
  myExp             EXCEPTION;
BEGIN
  IF Check_Staff_Account(Staff_No) = 0 THEN
    raise Account_Not_Exist;
  ELSIF check_admin(Admin_No, Admin_Password) = 0 THEN
    raise myExp;
  ELSE
    UPDATE Staff Set Password = Admin_No WHERE No = Staff_No;
  END IF;
EXCEPTION
  WHEN Account_Not_Exist THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ų����ڣ���ˢ�º�����$');
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20002, '$����Ա�˺��������$');
END default_Staff_Password;

--ͨ��ISBN�˶��鼮�Ƿ������ͼ�����ϵͳ��
CREATE OR REPLACE FUNCTION Check_Book_ISBN(Book_ISBN IN Book.Isbn%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Book WHERE Book.ISBN = Book_ISBN;
  RETURN flag;
END Check_Book_ISBN;

--ͨ��ISBN�Ͷ����˺�ȷ�϶����Ƿ�������
CREATE OR REPLACE FUNCTION Check_Reader_Really_Borrow_Book(Book_ISBN IN Book.Isbn%type,
                                                           Reader_No IN Reader_Borrow_Return_History.No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Reader_Borrow_Return_History
   WHERE Reader_Borrow_Return_History.ISBN = Book_ISBN
     AND Reader_Borrow_Return_History.No = Reader_No
     AND Reader_Borrow_Return_History.Returndate is null;
  RETURN flag;
END Check_Reader_Really_Borrow_Book;

--ͨ��ISBN�Ͷ����˺�ȷ�϶����Ƿ�������������
CREATE OR REPLACE FUNCTION Check_Reader_Apply_Borrow_Book(BookISBN IN Book.Isbn%type,
                                                          ReaderNo IN Staff_DealWith_Reader_Borrow_History.Reader_No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Staff_DealWith_Reader_Borrow_History
   WHERE Staff_DealWith_Reader_Borrow_History.ISBN = BookISBN
     AND Staff_DealWith_Reader_Borrow_History.Reader_No = ReaderNo
     AND Staff_DealWith_Reader_Borrow_History.Status = '������';
  RETURN flag;
END Check_Reader_Apply_Borrow_Book;

--ͨ��ISBN�Ͷ����˺�ȷ�϶����Ƿ�������黹����
CREATE OR REPLACE FUNCTION Check_Reader_Apply_Return_Book(BookISBN IN Book.Isbn%type,
                                                          ReaderNo IN Staff_DealWith_Reader_Borrow_History.Reader_No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Staff_DealWith_Reader_Return_History
   WHERE Staff_DealWith_Reader_Return_History.ISBN = BookISBN
     AND Staff_DealWith_Reader_Return_History.Reader_No = ReaderNo
     AND Staff_DealWith_Reader_Return_History.Status = '������';
  RETURN flag;
END Check_Reader_Apply_Return_Book;

--���ؾ�����ҳ������ݼ� ��sql, ҳ��С����ǰ�ǵڼ�ҳ���������
CREATE OR REPLACE FUNCTION paging_cursor(in_sql        in VARCHAR2,
                                         v_in_pagesize in NUMBER,
                                         v_in_pagenow  in NUMBER,
                                         DataSize out NUMBER)
  RETURN SYS_REFCURSOR IS
  res_cur SYS_REFCURSOR;
  v_start NUMBER;
  v_end   NUMBER;
  v_sql   VARCHAR2(1000);
BEGIN
  --����v_start��v_end�Ƕ���
  v_start := v_in_pagesize * (v_in_pagenow - 1) + 1;
  v_end   := v_in_pagesize * v_in_pagenow;
  
  execute immediate 'select count(*) from (' || in_sql || ')' into DataSize;
  --ִ�з�ҳ
  v_sql   := 'select t2.* from (select t1.*,rownum rn from (' || in_sql ||
             ') t1 where rownum<=' || v_end || ') t2 where rn>=' || v_start;
  --DBMS_OUTPUT.PUT_LINE(v_sql);
  open res_cur for v_sql;
  RETURN res_cur;
END paging_cursor;

--��ѯStaff���еĲ�����ʷ��¼
CREATE OR REPLACE FUNCTION query_staff_history(Staff_Account  in VARCHAR2,
                                               Staff_Password in VARCHAR2,
                                               pageNow        in NUMBER,
                                               pageSize       in NUMBER,
                                               DataSize       out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
  myExp EXCEPTION;
BEGIN
  IF Check_Staff(Staff_Account, Staff_Password) = 1 THEN
    insql := 'SELECT * FROM Staff_DealWith_Book_History NATURAL JOIN Book WHERE No=''' || Staff_Account || '''ORDER BY PurchaseDate desc';
    RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END query_staff_history;

--��ѯStaff��ISBN�йصĲ�����¼
CREATE OR REPLACE FUNCTION query_staff_history_in_isbn(Staff_Account  in VARCHAR2,
                                                       Staff_Password in VARCHAR2,
                                                       ISBN           IN VARCHAR2,
                                                       pageNow        in NUMBER,
                                                       pageSize       in NUMBER,
                                                       DataSize       out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
  myExp EXCEPTION;
BEGIN
  IF Check_Staff(Staff_Account, Staff_Password) = 1 THEN
    insql := 'SELECT * FROM Staff_DealWith_Book_History NATURAL JOIN Book WHERE No=''' ||
             Staff_Account || '''AND regexp_like(ISBN, ''' || ISBN ||
             '+'') ORDER BY PurchaseDate desc';
    RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END query_staff_history_in_isbn;

--��ѯ��������
CREATE OR REPLACE FUNCTION query_reader_list(pageNow  in NUMBER,
                                             pageSize in NUMBER,
                                             DataSize out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
  myExp EXCEPTION;
BEGIN
  insql := 'SELECT * FROM Reader_Details ORDER BY No ASC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END query_reader_list;

-- ���ؼ��ֵĶ��߲�ѯ
CREATE OR REPLACE FUNCTION query_reader_list_in_keyword(keyword  in varchar2,
                                                        pageNow  in NUMBER,
                                                        pageSize in NUMBER,
                                                        DataSize out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Reader_Details WHERE regexp_like(Reader_Details.No, ''' ||
           keyword || '+'')' || ' ORDER BY No ASC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_reader_list_in_keyword;

--��ѯStaff����
CREATE OR REPLACE FUNCTION query_staff_list(pageNow  in NUMBER,
                                             pageSize in NUMBER,
                                             DataSize out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Staff ORDER BY No ASC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_staff_list;

-- ���ؼ��ֵ�staff��ѯ
CREATE OR REPLACE FUNCTION query_staff_list_in_keyword(keyword  in varchar2,
                                                        pageNow  in NUMBER,
                                                        pageSize in NUMBER,
                                                        DataSize out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Staff WHERE regexp_like(Staff.No, ''' ||
           keyword || '+'')' || ' ORDER BY No ASC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_staff_list_in_keyword;

--��ȡ����Ա�˺�����
CREATE OR REPLACE FUNCTION get_Admin_Details(Admin_Account  in VARCHAR2,
                                             Admin_Password in VARCHAR2)
  RETURN SYS_REFCURSOR IS
  cur SYS_REFCURSOR;
  myExp EXCEPTION;
BEGIN
  IF Check_Admin(Admin_Account, Admin_Password) = 1 THEN
    open cur FOR
      select *
        from Admin
       where Admin_Account = no
         AND Admin_Password = password;
    RETURN cur;
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END get_Admin_Details;

--��ȡStaff�˺�����
CREATE OR REPLACE FUNCTION get_Staff_Details(Staff_Account  in VARCHAR2,
                                             Staff_Password in VARCHAR2)
  RETURN SYS_REFCURSOR IS
  cur SYS_REFCURSOR;
  myExp EXCEPTION;
BEGIN
  IF Check_Staff(Staff_Account, Staff_Password) = 1 THEN
    open cur FOR
      select *
        from Staff
       where Staff_Account = no
         AND Staff_Password = password;
    RETURN cur;
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END get_Staff_Details;

--��ȡ�����˺�����
CREATE OR REPLACE FUNCTION get_Reader_Details(Reader_Account  in VARCHAR2,
                                              Reader_Password in VARCHAR2)
  RETURN SYS_REFCURSOR IS
  cur SYS_REFCURSOR;
  myExp EXCEPTION;
BEGIN
  IF check_reader(Reader_Account, Reader_Password) = 1 THEN
    open cur FOR
      select *
        from Reader_Details
       where Reader_Account = no;
    RETURN cur;
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END get_Reader_Details;

--��ȡ���ߵ���ʷ��¼
CREATE OR REPLACE FUNCTION query_reader_history(Reader_Account in VARCHAR2,
                                                Reader_Password in VARCHAR2,
                                                pageNow        in NUMBER,
                                                pageSize       in NUMBER,
                                                DataSize       out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
  myExp EXCEPTION;
BEGIN
  IF check_reader(Reader_Account, Reader_Password) = 1 THEN
    insql := 'SELECT * FROM Reader_Borrow_Return_History NATURAL JOIN Book WHERE no=''' || Reader_Account || '''order by ReturnDate  desc nulls first';
    RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END query_reader_history;

--��ѯ���ߺ�ISBN�йص���ʷ��¼
CREATE OR REPLACE FUNCTION query_reader_history_in_isbn(Reader_Account  in VARCHAR2,
                                               Reader_Password in VARCHAR2,
                                               ISBN IN Book.Isbn%type,
                                               pageNow        in NUMBER,
                                               pageSize       in NUMBER,
                                               DataSize       out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
  myExp EXCEPTION;
BEGIN
  IF Check_Admin(Reader_Account, Reader_Password) = 1 THEN
    insql := 'SELECT * FROM Reader_Borrow_Return_History NATURAL JOIN Book WHERE no=''' || Reader_Account || '''AND regexp_like(ISBN, '''|| ISBN || '+'') order by ReturnDate  desc nulls first ';
    RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END query_reader_history_in_isbn;

-- ��ѯ�鼮����
CREATE OR REPLACE PROCEDURE query_book_type(cur out SYS_REFCURSOR) AS
BEGIN
  open cur for
    select * from book_types;
END query_book_type;

--a)	���ͼ�飺Staff�˺ţ�����, ISBN�����������ߣ������磬����ʱ�䣬���ͣ�����(�ɲ���Ҫ)��
CREATE OR REPLACE PROCEDURE add_Book(Staff_Account    Staff.No%type,
                                     Staff_PassWord   Staff.PassWord%type,
                                     Book_ISBN        Book.Isbn%type,
                                     Book_Name        Book.Name%type,
                                     Book_Author      Book.Author%type,
                                     Book_Type        VARCHAR2,
                                     Book_Publisher   Book.Publisher%type,
                                     Book_PublishDate Book.Publishdate%type,
                                     Book_Value       Book.Value%type,
                                     Book_Amount      Book.Amount%type := 0,
                                     Book_Available   Book.Available%type := 0,
                                     Book_Cover       Book.Cover%type := 'null') AS
  myExp EXCEPTION;
  flag number;
BEGIN
  IF Check_Staff(Staff_Account, Staff_PassWord) = 1 THEN
    SELECT Type_No into flag FROM Book_Types WHERE Type = Book_Type;
    INSERT INTO Book
    VALUES
      (Book_ISBN,
       Book_Name,
       Book_Author,
       flag,
       Book_Publisher,
       Book_PublishDate,
       Book_Value,
       Book_Amount,
       Book_Available,
       Book_Cover);
  ELSE
    raise myExp;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
  WHEN Dup_val_on_index THEN
    RAISE_APPLICATION_ERROR(-20002, '$�鼮�Ѵ���$');
  WHEN no_data_found THEN
    RAISE_APPLICATION_ERROR(-20003, '$�鼮���ʹ���$');
END add_Book;

--b)	����ͼ�飺ISBN������Ա�˺ţ����룬���������
CREATE OR REPLACE PROCEDURE increase_Book(Book_ISBN      Book.Isbn%type,
                                          Quantity       Book.Amount%type,
                                          Staff_Account  Staff.No%type,
                                          Staff_PassWord Staff.PassWord%type) AS
  -- û���ҵ��鼮�쳣
  NoBook EXCEPTION;
  -- ����Ϊ�����쳣
  NonNegative EXCEPTION;
  -- �˺���������쳣
  AccPasErr EXCEPTION;
BEGIN
  IF Check_Staff(Staff_Account, Staff_PassWord) = 1 THEN
    IF Check_Book_ISBN(Book_ISBN) = 0 THEN
      raise NoBook;
    ELSIF Quantity < 0 THEN
      raise NonNegative;
    ELSE
      --�����鼮��Ϣ
      UPDATE Book
         SET Book.Amount    = Book.Amount + Quantity,
             Book.Available = Book.Available + Quantity
       WHERE Book.Isbn = Book_ISBN;
      -- ��ӹ���Ա���鼮��
      INSERT INTO Staff_DealWith_Book_History
      VALUES
        (Staff_Account, Book_ISBN, sysdate, Quantity);
    END IF;
  ELSE
    raise AccPasErr;
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20003, '$û���ҵ����鼮$');
  WHEN NonNegative THEN
    RAISE_APPLICATION_ERROR(-20004, '$��������Ϊ����$');
  WHEN AccPasErr THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END increase_Book;

--c)	ɾ��ͼ�飺ISBN��Staff�˺ţ����룬ɾ��������
CREATE OR REPLACE PROCEDURE decrease_Book(Book_ISBN      Book.Isbn%type,
                                          Quantity       Book.Amount%type,
                                          Staff_Account  Staff.No%type,
                                          Staff_PassWord Staff.PassWord%type) AS
  Book_Available number;
  -- û���ҵ��鼮�쳣
  NoBook EXCEPTION;
  -- ����Ϊ�����쳣
  NonNegative EXCEPTION;
  -- ����������������
  AmountLimit EXCEPTION;
  -- �˺���������쳣
  AccPasErr EXCEPTION;
BEGIN
  IF Check_Staff(Staff_Account, Staff_PassWord) = 1 THEN
    IF Check_Book_ISBN(Book_ISBN) = 0 THEN
      raise NoBook;
    ELSIF Quantity < 0 THEN
      raise NonNegative;
    ELSE
      SELECT Book.Available
        INTO Book_Available
        FROM Book
       WHERE Book.Isbn = Book_ISBN;
      IF Book_Available - Quantity < 0 THEN
        raise AmountLimit;
      ELSE
        --�����鼮��Ϣ
        UPDATE Book
           SET Book.Amount    = Book.Amount - Quantity,
               Book.Available = Book.Available - Quantity
         WHERE Book.Isbn = Book_ISBN;
        -- ���¹���Ա���鼮��
        INSERT INTO Staff_DealWith_Book_History
        VALUES
          (Staff_Account, Book_ISBN, sysdate, 0 - Quantity);
      END IF;
    END IF;
  ELSE
    raise AccPasErr;
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20003, '$û���ҵ����鼮$');
  WHEN NonNegative THEN
    RAISE_APPLICATION_ERROR(-20004, '$��������Ϊ����$');
  WHEN AccPasErr THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
  WHEN AmountLimit THEN
    RAISE_APPLICATION_ERROR(-20005, '$����������������$');
END decrease_Book;

--d)  �޸�ͼ����Ϣ��ͼ������ԣ���ǰ��¼�˺ţ����롣
CREATE OR REPLACE PROCEDURE mod_Book(Staff_Account    Staff.No%type,
                                     Staff_PassWord   Staff.PassWord%type,
                                     Book_ISBN        Book.Isbn%type,
                                     Book_Name        Book.Name%type,
                                     Book_Author      Book.Author%type,
                                     Book_Type        VARCHAR2,
                                     Book_Publisher   Book.Publisher%type,
                                     Book_PublishDate Book.Publishdate%type,
                                     Book_Value       Book.Value%type,
                                     Book_Cover       Book.Cover%type) AS
  -- û���ҵ��鼮�쳣
  NoBook EXCEPTION;
  -- �˺���������쳣
  AccPasErr EXCEPTION;
  -- 
  flag number;
BEGIN
  IF Check_Staff(Staff_Account, Staff_PassWord) = 0 THEN
    raise AccPasErr;
  ELSIF Check_Book_ISBN(Book_ISBN) = 0 THEN
    raise NoBook;
  ELSE
    SELECT Type_No into flag FROM Book_Types WHERE Type = Book_Type;
    UPDATE Book
       SET Name        = Book_Name,
           Author      = Book_Author,
           Type_No     = flag,
           Publisher   = Book_Publisher,
           PublishDate = Book_PublishDate,
           Value       = Book_Value,
           Cover       = Book_Cover
     WHERE ISBN = Book_ISBN;
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20003, '$û���ҵ����鼮$');
  WHEN AccPasErr THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ż��������$');
END mod_Book;

--e)	ע���˺ţ�ע���˺�����Ҫ�����ݡ�
--����Ա
CREATE OR REPLACE PROCEDURE register_Account_Of_Admin(Admin_No       Admin.No%type,
                                                      Admin_Password Admin.Password%type,
                                                      Admin_Name     Admin.Name%type,
                                                      Admin_Gender   Admin.Gender%type,
                                                      Admin_Phone    Admin.Phone%type) AS
BEGIN
  INSERT INTO Admin
  VALUES
    (Admin_No, Admin_Password, Admin_Name, Admin_Gender, Admin_Phone);
EXCEPTION
  WHEN Dup_val_on_index THEN
    RAISE_APPLICATION_ERROR(-20002, '$�˺��Ѵ���$');
END register_Account_Of_Admin;
--����
CREATE OR REPLACE PROCEDURE register_Account_Of_Reader(Reader_No       Reader_Account.No%type,
                                                       Reader_Password Reader_Account.Password%type,
                                                       Reader_Name     Reader_Details.Name%type,
                                                       Reader_Gender   Reader_Details.Gender%type,
                                                       Reader_Fine     Reader_Details.Fine%type) AS
BEGIN
  --�Ƚ��˺��������
  INSERT INTO Reader_Account VALUES (Reader_No, Reader_Password);
  --�ٴ����������
  INSERT INTO Reader_Details
  VALUES
    (Reader_No,
     Reader_Name,
     Reader_Gender,
     Reader_Fine,
     sysdate,
     add_months(sysdate, 12));
EXCEPTION
  WHEN Dup_val_on_index THEN
    RAISE_APPLICATION_ERROR(-20002, '$�˺��Ѵ���$');
END register_Account_Of_Reader;
--Staff
CREATE OR REPLACE PROCEDURE register_Account_Of_Staff(Staff_No       Staff.No%type,
                                                      Staff_Password Staff.Password%type,
                                                      Staff_Name     Staff.Name%type,
                                                      Staff_Gender   Staff.Gender%type,
                                                      Staff_Phone    Staff.Phone%type) AS
BEGIN
  INSERT INTO Staff
  VALUES
    (Staff_No, Staff_Password, Staff_Name, Staff_Gender, Staff_Phone);
EXCEPTION
  WHEN Dup_val_on_index THEN
    RAISE_APPLICATION_ERROR(-20002, '$�˺��Ѵ���$');
END register_Account_Of_Staff;

--f)	�޸��˺���Ϣ
--����Ա
CREATE OR REPLACE PROCEDURE mod_Account_Of_Admin(Admin_No       Admin.No%type,
                                                 Admin_Password Admin.Password%type,
                                                 Admin_Name     Admin.Name%type,
                                                 Admin_Gender   Admin.Gender%type,
                                                 Admin_Phone    Admin.Phone%type) AS
BEGIN
  UPDATE Admin
     SET Password = Admin_Password,
         Name     = Admin_Name,
         Gender   = Admin_Gender,
         Phone    = Admin_Phone
   WHERE No = Admin_No;
END mod_Account_Of_Admin;

--����
CREATE OR REPLACE PROCEDURE mod_Account_Of_Reader(Reader_No       Reader_Account.No%type,
                                                  Reader_Password Reader_Account.Password%type,
                                                  Reader_Name     Reader_Details.Name%type,
                                                  Reader_Gender   Reader_Details.Gender%type,
                                                  Reader_Fine     Reader_Details.Fine%type) AS
BEGIN
  --���޸�����
  UPDATE Reader_Account
     SET Password = Reader_Password
   WHERE No = Reader_No;
  --���޸ĸ�������
  UPDATE Reader_Details
     SET Name = Reader_Name, Gender = Reader_Gender, Fine = Reader_Fine
   WHERE No = Reader_No;
END mod_Account_Of_Reader;
--staff
CREATE OR REPLACE PROCEDURE mod_Account_Of_Staff(Staff_No       Staff.No%type,
                                                 Staff_Password Staff.Password%type,
                                                 Staff_Name     Staff.Name%type,
                                                 Staff_Gender   Staff.Gender%type,
                                                 Staff_Phone    Staff.Phone%type) AS
BEGIN
  UPDATE Staff
     SET Password = Staff_Password,
         Name     = Staff_Name,
         Gender   = Staff_Gender,
         Phone    = Staff_Phone
   WHERE No = Staff_No;
END mod_Account_Of_Staff;

--g)	ɾ���˺ţ���ǰ��¼�˺ţ����롣
--����Ա
CREATE OR REPLACE PROCEDURE del_Account_Of_Admin(Admin_No Admin.No%type) AS
  myExp EXCEPTION;
BEGIN
  IF check_admin_account(Admin_No) = 0 THEN
    raise myExp;
  ELSE
    DELETE FROM Admin WHERE Admin_No = No;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ų�����$');
END del_Account_Of_Admin;

--����
CREATE OR REPLACE PROCEDURE del_Account_Of_Reader(Reader_No Reader_Account.No%type) AS
  myExp EXCEPTION;
BEGIN
  IF check_reader_account(Reader_No) = 0 THEN
    raise myExp;
  ELSE
    DELETE FROM Reader_Account WHERE Reader_No = No;
    DELETE FROM Reader_Details WHERE Reader_No = No;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ų�����$');
END del_Account_Of_Reader;
--staff
CREATE OR REPLACE PROCEDURE del_Account_Of_Staff(Staff_No Staff.No%type) AS
  myExp EXCEPTION;
BEGIN
  IF Check_Staff_Account(Staff_No) = 0 THEN
    raise myExp;
  ELSE
    DELETE FROM Staff WHERE Staff_No = No;
  END IF;
EXCEPTION
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20001, '$�˺Ų�����$');
END del_Account_Of_Staff;

--h)	ͼ���ѯ��ͼ��Ĳ������Ե���ϡ�
--�����ģ����ѯ
CREATE OR REPLACE FUNCTION query_all(word     in VARCHAR2,
                                     pageNow  in NUMBER,
                                     pageSize in NUMBER,
                                     DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book NATURAL JOIN Book_Types ' ||
           'WHERE regexp_like(Book.ISBN, ''' || word || '+'')' ||
           ' OR regexp_like(Book.Name, ''' || word || '+'')' ||
           ' OR regexp_like(Book.Author, ''' || word || '+'')' ||
           ' OR regexp_like(Type, ''' || word || '+'')' ||
           ' OR regexp_like(Book.Publisher, ''' || word || '+'')' ||
           ' OR regexp_like(to_char(Book.PublishDate, ''yyyy/mm''), ''' || word ||
           '+'')' || ' ORDER BY Book.ISBN';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_all;

--�ؼ���ģ����ѯ
CREATE OR REPLACE FUNCTION query_key_word(parameter in varchar2,
                                          word      in varchar2,
                                          pageNow   in NUMBER,
                                          pageSize  in NUMBER,
                                          DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  CASE upper(parameter)
    WHEN 'ISBN' THEN
      insql := 'SELECT * FROM Book NATURAL JOIN Book_Types WHERE regexp_like(Book.ISBN, ''' || word ||
               '+'') ORDER BY Book.ISBN';
      RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
    WHEN 'NAME' THEN
      insql := 'SELECT * FROM Book NATURAL JOIN Book_Types WHERE regexp_like(Book.Name, ''' || word ||
               '+'') ORDER BY Book.ISBN';
      RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
    WHEN 'AUTHOR' THEN 
      insql := 'SELECT * FROM Book NATURAL JOIN Book_Types WHERE regexp_like(Book.Author, ''' || word ||
               '+'') ORDER BY Book.ISBN';
      RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
    WHEN 'TYPE' THEN
      insql := 'SELECT * FROM Book NATURAL JOIN Book_Types WHERE regexp_like(Type, ''' || word ||
               '+'') ORDER BY Book.ISBN';
      RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
    WHEN 'PUBLISHER' THEN
      insql := 'SELECT * FROM Book NATURAL JOIN Book_Types WHERE regexp_like(Book.Publisher, ''' || word ||
               '+'') ORDER BY Book.ISBN';
      RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
    WHEN 'PUBLISHDATE' THEN
      insql := 'SELECT * FROM Book NATURAL JOIN Book_Types WHERE regexp_like(to_char(Book.PublishDate, ''yyyy/mm''), ''' || word ||
               '+'') ORDER BY Book.ISBN';
      RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
    ELSE
      DBMS_OUTPUT.PUT_LINE('�ؼ��ʴ���');
  END CASE;
END query_key_word;

--i)	����ͼ�飺ISBN�������˺š�
CREATE OR REPLACE PROCEDURE Borrow_Book(Book_ISBN Book.isbn%type,
                                        Reader_No Reader_Account.No%type) AS
  -- û���ҵ��鼮�쳣
  NoBook EXCEPTION;
  -- �ظ������쳣
  TwiceBorrowErr EXCEPTION;
  -- �ݲ�������
  AmountLimit EXCEPTION;
  -- �ظ������쳣
  RePlayApply EXCEPTION;
  -- �����쳣
  ExistFine EXCEPTION;
  -- ʣ��ݲ���
  Book_Available NUMBER;
BEGIN
  Select Fine INTO Book_Available From Reader_Details WHERE Reader_No = No;
  IF check_book_isbn(Book_ISBN) = 0 THEN
    raise NoBook;
  ELSIF Check_Reader_Really_Borrow_Book(Book_ISBN, Reader_No) >= 1 THEN
    raise TwiceBorrowErr;
  ELSIF Check_Reader_Apply_Borrow_Book(Book_ISBN, Reader_No) >= 1 THEN
    raise RePlayApply;
  ELSIF Book_Available > 0 THEN
    raise ExistFine;
  ELSE
    SELECT Book.Available
      INTO Book_Available
      FROM Book
     WHERE Book.Isbn = Book_ISBN;
    IF Book_Available < 1 THEN
      raise AmountLimit;
    ELSE
      UPDATE Book
         Set Book.Available = Book.Available - 1
       Where Book_ISBN = Book.Isbn;
      INSERT INTO Staff_DealWith_Reader_Borrow_History
      VALUES
        (Reader_No, Book_ISBN, sysdate, null, '������');
    END IF;
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20003, '$û���ҵ����鼮$');
  WHEN TwiceBorrowErr THEN
    RAISE_APPLICATION_ERROR(-20006, '$����ͬʱ����ͬһ�鼮$');
  WHEN AmountLimit THEN
    RAISE_APPLICATION_ERROR(-20005, '$�ݲ�������$');
  WHEN RePlayApply THEN
    RAISE_APPLICATION_ERROR(-20002, '$���Ѿ�������ģ��벻Ҫ�ظ�����$');
  WHEN ExistFine THEN
    RAISE_APPLICATION_ERROR(-20007, '$���ڷ���δ���ɣ�����ɷ��������$');
END Borrow_Book;

--j)	�黹ͼ�飺ISBN�������˺ţ����롣
CREATE OR REPLACE PROCEDURE Return_Book(Book_ISBN Book.isbn%type,
                                        Reader_No Reader_Account.No%type) AS
  -- û���ҵ��鼮�쳣
  NoBook EXCEPTION;
  -- �ظ������쳣
  RePlayApply EXCEPTION;
BEGIN
  IF Check_Reader_Really_Borrow_Book(Book_ISBN, Reader_No) = 0 THEN
    raise NoBook;
  ELSIF Check_Reader_Apply_Return_Book(Book_ISBN, Reader_No) >= 1 THEN
    raise RePlayApply;
  ELSE
    -- �������Ա�黹�����б�
    INSERT INTO Staff_DealWith_Reader_Return_History
    VALUES
      (Reader_No, Book_ISBN, sysdate, null, '������');
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20001,
                            '$���鲻��ͼ��ݻ�δ���˺����ҵ�����Ľ��ļ�¼$');
  WHEN RePlayApply THEN
    RAISE_APPLICATION_ERROR(-20002, '$�벻Ҫ�ظ�����$');
END Return_Book;

--Staff�������ͼ��
CREATE OR REPLACE PROCEDURE DealWith_Borrow_Book(parameter VARCHAR2,
                                                 BookISBN Book.isbn%type,
                                                 ReaderNo Reader_Account.No%type,
                                                 StaffNo  Staff_DealWith_Reader_Borrow_History.Staff_No%type) AS
  -- �����쳣
  ExistFine EXCEPTION;
  -- Ӧ������
  ShouldPayFine NUMBER;
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF check_reader_apply_borrow_book(BookISBN, ReaderNo) = 0 THEN
     raise StatusError;
  END IF;
  CASE parameter
    WHEN '"ͬ��"' THEN
      -- ����Ƿ���ڷ���
      Select Fine
        INTO ShouldPayFine
        From Reader_Details
       WHERE ReaderNo = No;
      IF ShouldPayFine > 0 THEN
        raise ExistFine;
      END IF;
      -- ���½��������¼
      UPDATE Staff_DealWith_Reader_Borrow_History
         Set Staff_DealWith_Reader_Borrow_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Borrow_History.Status   = '����ɹ�'
       WHERE Staff_DealWith_Reader_Borrow_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Borrow_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Borrow_History.Status = '������';
      -- ���¶��߽��ļ�¼
      INSERT INTO Reader_Borrow_Return_History
      Values
        (ReaderNo, BookISBN, sysdate, add_months(sysdate, 1), null);
      -- �����߷���Ϣ
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '��������',
         '��ϲ�������Ľ�������(ISBN:' || BookISBN || ')�Ѿ����ͨ���������鼮���������XXX����������ȡ�飡',
         'δ�鿴');
    WHEN '"�ܾ�"' THEN
      -- ���½��������¼
      UPDATE Staff_DealWith_Reader_Borrow_History
         Set Staff_DealWith_Reader_Borrow_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Borrow_History.Status   = '����ʧ��'
       WHERE Staff_DealWith_Reader_Borrow_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Borrow_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Borrow_History.Status = '������';
      -- ���鼮������������
      UPDATE Book
         Set Book.Available = Book.Available + 1
       Where BookISBN = Book.Isbn;
      -- �����߷�����Ϣ
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '��������',
         '��Ǹ����ΪXXX, ���Ľ�������(ISBN:' || BookISBN || ')δͨ����ˣ����Ժ�����',
         'δ�鿴');
    ELSE
      raise StatusError;
  END CASE;
EXCEPTION
  WHEN ExistFine THEN
    RAISE_APPLICATION_ERROR(-20007, '$���ڷ���δ���ɣ�����ɷ��������$');
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$�鼮״̬�Ѹ��£���ˢ�º�����$');
END DealWith_Borrow_Book;
    
--Staff����黹ͼ��
CREATE OR REPLACE PROCEDURE DealWith_Return_Book(parameter VARCHAR2,
                                                 BookISBN  VARCHAR2,
                                                 ReaderNo  VARCHAR2,
                                                 StaffNo   VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
  -- Time_Day
  Days        INT;
  should_Date Date;
  -- ����NO
  SubReaderNo VARCHAR2(50);
BEGIN
  IF check_reader_apply_return_book(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  CASE parameter
    WHEN '"ͬ��"' THEN
      -- ����黹�����
      UPDATE Staff_DealWith_Reader_Return_History
         Set Staff_DealWith_Reader_Return_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Return_History.Status   = '����ɹ�'
       WHERE Staff_DealWith_Reader_Return_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Return_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Return_History.Status = '������';
      --������
      --��������
      Select Shouldreturndate
        INTO should_Date
        From Reader_Borrow_Return_History
       WHERE No = ReaderNo
         AND ISBN = BookISBN
         AND ReturnDate IS NULL;
      Days := TO_NUMBER(sysdate - should_date);
      DBMS_OUTPUT.PUT_LINE(Days);
      --�����������30��
      IF Days >= 30 THEN
        UPDATE Reader_Details
           SET Fine = Fine + (Days - 29) * 0.6
         WHERE ReaderNo = No;
      END IF;
      -- ���¶��߽��ı�
      UPDATE Reader_Borrow_Return_History
         Set ReturnDate = sysdate
       WHERE No = ReaderNo
         AND ISBN = BookISBN
         AND ReturnDate is null;
      -- �����߷�����Ϣ
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '�黹����',
         '��ϲ�������Ļ�������(ISBN:' || BookISBN ||
         ')�Ѿ����ͨ��������xxxʱ���ڽ�����õ�xxx��̨�������й�����ԱΪ������',
         'δ�鿴');
      -- ���鼮������������
      UPDATE Book
         Set Book.Available = Book.Available + 1
       Where BookISBN = Book.Isbn;
      -- ���鼮����ԤԼ�Ķ���
      IF query_Book_Subscribe(BookISBN) > 0 THEN
        -- ��ѯ��һ��ԤԼ�Ķ���
        select No
          INTO SubReaderNo
          from (select *
                  from Book_Subscribe
                 WHERE ISBN = BookISBN
                   AND Status = '��ԤԼ'
                 order by Time asc)
         where rownum = 1;
        -- ����ԤԼ��
        UPDATE Book_Subscribe
           SET Status = '�����'
         WHERE SubReaderNo = No
           AND ISBN = BookISBN
           AND Status = '��ԤԼ';
        -- ���鼮����ԤԼ�Ķ��߽��ı�
        INSERT INTO Reader_Borrow_Return_History
        Values
          (SubReaderNo, BookISBN, sysdate, add_months(sysdate, 1), null);
      END IF;
    WHEN '"�ܾ�"' THEN
      -- ����黹��¼
      UPDATE Staff_DealWith_Reader_Return_History
         Set Staff_DealWith_Reader_Return_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Return_History.Status   = '����ʧ��'
       WHERE Staff_DealWith_Reader_Return_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Return_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Return_History.Status = '������';
      -- �����߷�����Ϣ
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '�黹����',
         '��Ǹ����ΪXXX, ���Ļ�������(ISBN:' || BookISBN || ')δͨ����ˣ����Ժ�����',
         'δ�鿴');
    ELSE
      raise StatusError;
  END CASE;
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$�鼮״̬�Ѹ��£���ˢ�º�����$');
END DealWith_Return_Book;

--��ѯ���߽�������״̬
CREATE OR REPLACE FUNCTION query_Staff_DealWith_Reader_Borrow_History(Reader_No in Reader_Account.No%type,
                                                                      pageNow   in NUMBER,
                                                                      pageSize  in NUMBER,
                                                                      DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_BORROW_HISTORY NATURAL JOIN Book ' ||
           'WHERE Reader_No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''������'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Borrow_History;

--��ѯ���߽�������״̬���ؼ���
CREATE OR REPLACE FUNCTION query_Staff_DealWith_Reader_Borrow_History_With_Keyword(Reader_No in Reader_Account.No%type,
                                                                                   keyword   in VARCHAR2,
                                                                                   pageNow   in NUMBER,
                                                                                   pageSize  in NUMBER,
                                                                                   DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_BORROW_HISTORY NATURAL JOIN Book ' ||
           'WHERE Reader_No = ''' || Reader_No ||
           '''AND regexp_like(ISBN, '''|| keyword ||
           '+'') ORDER BY decode(Status, ''������'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);   
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Borrow_History_With_Keyword;

--��ѯ���߻�������
CREATE OR REPLACE FUNCTION query_Staff_DealWith_Reader_Return_History(Reader_No in Reader_Account.No%type,
                                                                      pageNow   in NUMBER,
                                                                      pageSize  in NUMBER,
                                                                      DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_RETURN_HISTORY NATURAL JOIN Book ' ||
           'WHERE Reader_No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''������'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Return_History;

--��ѯ���߻���������ؼ���
CREATE OR REPLACE FUNCTION query_Staff_DealWith_Reader_Return_History_With_Keyword(Reader_No in Reader_Account.No%type,
                                                                                   keyword   in VARCHAR2,
                                                                                   pageNow   in NUMBER,
                                                                                   pageSize  in NUMBER,
                                                                                   DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_RETURN_HISTORY NATURAL JOIN Book ' ||
           'WHERE Reader_No = ''' || Reader_No ||
           '''AND regexp_like(ISBN, '''|| keyword ||
           '+'') ORDER BY decode(Status, ''������'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Return_History_With_Keyword;


--��ȡ���еĻ�������
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Return_History(pageNow  in NUMBER,
                                                                    pageSize in NUMBER,
                                                                    DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_RETURN_HISTORY NATURAL JOIN Book ' ||
           'ORDER BY decode(Status, ''������'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Return_History;

--��ȡ���еĻ���������ؼ���
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Return_History_With_Keyword(keyword  in VARCHAR2,
                                                                                 pageNow  in NUMBER,
                                                                                 pageSize in NUMBER,
                                                                                 DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_RETURN_HISTORY NATURAL JOIN Book ' ||
           'WHERE regexp_like(Reader_No, ''' || keyword ||
           '+'') ORDER BY decode(Status, ''������'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Return_History_With_Keyword;


--��ȡ���н�������״̬
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Borrow_History(pageNow  in NUMBER,
                                                                    pageSize in NUMBER,
                                                                    DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_BORROW_HISTORY NATURAL JOIN Book ' ||
           'ORDER BY decode(Status, ''������'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Borrow_History;

--��ѯ���н�������״̬���ؼ���
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Borrow_History_With_Keyword(keyword  in VARCHAR2,
                                                                                 pageNow  in NUMBER,
                                                                                 pageSize in NUMBER,
                                                                                 DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_BORROW_HISTORY NATURAL JOIN Book ' ||
           'WHERE regexp_like(ISBN, ''' || keyword ||
           '+'') ORDER BY decode(Status, ''������'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Borrow_History_With_Keyword;


-- ��ȡ������Ϣ
CREATE OR REPLACE FUNCTION get_Reader_Message(Reader_No in Reader_Account.No%type,
                                              pageNow   in NUMBER,
                                              pageSize  in NUMBER,
                                              DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Reader_Message ' ||
           'WHERE No = ''' || Reader_No ||
           ''' ORDER BY Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Reader_Message;

-- ������Ϣ�Ѷ�
CREATE OR REPLACE FUNCTION set_Reader_Message(Reader_No in Reader_Account.No%type,
                                              t         in Date)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  UPDATE Reader_Message
     SET Status = '�Ѷ�'
   WHERE No = Reader_No
     AND Time = t
     AND Status = 'δ�鿴';
END set_Reader_Message;

-- ȡ����������
CREATE OR REPLACE PROCEDURE cancel_Reader_Borrow_Apply(BookISBN VARCHAR2,
                                                       ReaderNo VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF check_reader_apply_borrow_book(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  -- ���½��������¼
  UPDATE Staff_DealWith_Reader_Borrow_History
     Set Staff_DealWith_Reader_Borrow_History.Status = '��ȡ��'
   WHERE Staff_DealWith_Reader_Borrow_History.Reader_No = ReaderNo
     AND Staff_DealWith_Reader_Borrow_History.ISBN = BookISBN
     AND Staff_DealWith_Reader_Borrow_History.Status = '������';
  -- ���鼮������������
  UPDATE Book
     Set Book.Available = Book.Available + 1
   Where BookISBN = Book.Isbn;
  -- �����߷�����Ϣ
  INSERT INTO Reader_Message
  VALUES
    (ReaderNo,
     sysdate,
     '��������',
     '���Ľ�������(ISBN:' || BookISBN || ')�ѳɹ�ȡ������ӭ���´ν��ģ�',
     'δ�鿴');
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$�鼮״̬�Ѹ��£���ˢ�º�����$');
END cancel_Reader_Borrow_Apply;

--ȡ���黹����
CREATE OR REPLACE PROCEDURE cancel_Reader_Return_Apply(BookISBN VARCHAR2,
                                                       ReaderNo VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF check_reader_apply_return_book(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  -- ���½��������¼
  UPDATE Staff_DealWith_Reader_Return_History
     Set Staff_DealWith_Reader_Return_History.Status = '��ȡ��'
   WHERE Staff_DealWith_Reader_Return_History.Reader_No = ReaderNo
     AND Staff_DealWith_Reader_Return_History.ISBN = BookISBN
     AND Staff_DealWith_Reader_Return_History.Status = '������';
  -- �����߷�����Ϣ
  INSERT INTO Reader_Message
  VALUES
    (ReaderNo,
     sysdate,
     '��������',
     '���Ļ�������(ISBN:' || BookISBN || ')�ѳɹ�ȡ����',
     'δ�鿴');
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$�鼮״̬�Ѹ��£���ˢ�º�����$');
END cancel_Reader_Return_Apply;

-- ��ѯͼ���Ƿ����ڱ�����
CREATE OR REPLACE FUNCTION Check_Book_Request(BookISBN IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Book_Request
   WHERE BookISBN = Book_Request.Isbn
     AND Book_Request.status = '������';
  RETURN flag;
END Check_Book_Request;

-- ��ѯͼ���Ƿ����ڱ�ĳ���߼���
CREATE OR REPLACE FUNCTION Check_Reade_Book_Request(BookISBN IN VARCHAR2,
                                                    ReaderNo IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Book_Request
   WHERE BookISBN = Book_Request.Isbn
     AND Book_Request.status = '������'
     AND No = ReaderNo;
  RETURN flag;
END Check_Reade_Book_Request;

--Reader����ͼ��
CREATE OR REPLACE PROCEDURE reader_book_request(ReaderNo        IN VARCHAR2,
                                                BookISBN        IN VARCHAR2,
                                                BookName        IN VARCHAR2,
                                                BookAuthor      IN VARCHAR2 := null,
                                                BookPublisher   VARCHAR2 := null,
                                                BookPublishDate IN Date := null) AS
  BookExist  EXCEPTION;
  BookReady  EXCEPTION;
  ReaderBook EXCEPTION;
BEGIN
  IF check_book_isbn(BookISBN) = 1 THEN
    raise BookExist;
  ELSIF Check_Book_Request(BookISBN) = 1 THEN
    raise BookReady;
  ELSIF Check_Reade_Book_Request(BookISBN, ReaderNo) = 1 THEN
    raise ReaderBook;
  END IF;
  INSERT INTO Book_Request
  VALUES
    (ReaderNo,
     BookISBN,
     BookName,
     BookAuthor,
     BookPublisher,
     BookPublishDate,
     sysdate,
     '������',
     null);
EXCEPTION
  WHEN BookExist THEN
    RAISE_APPLICATION_ERROR(-20001, '$�鼮�Ѵ��ڣ��޷�����$');
  WHEN BookReady THEN
    RAISE_APPLICATION_ERROR(-20002, '$����Ա������˴��鼮�����Ժ�����$');
  WHEN ReaderBook THEN
    RAISE_APPLICATION_ERROR(-20002,
                            '$���ļ���������������У��벻Ҫ�ظ�����$');
END reader_book_request;

--����ȡ����������
CREATE OR REPLACE PROCEDURE cancel_Book_Request(BookISBN VARCHAR2,
                                                ReaderNo VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF Check_Reade_Book_Request(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  -- ���½��������¼
  UPDATE Book_Request
     Set Book_Request.Status = '��ȡ��'
   WHERE Book_Request.No = ReaderNo
     AND Book_Request.ISBN = BookISBN
     AND Book_Request.Status = '������';
  -- �����߷�����Ϣ
  INSERT INTO Reader_Message
  VALUES
    (ReaderNo,
     sysdate,
     '��������',
     '������ͼ������(ISBN:' || BookISBN || ')�ѳɹ�ȡ����',
     'δ�鿴');
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$�鼮״̬�Ѹ��£���ˢ�º�����$');
END cancel_Book_Request;

-- Staff�����������
CREATE OR REPLACE PROCEDURE DealWith_Book_Request(parameter VARCHAR2,
                                                  BookISBN  VARCHAR2,
                                                  ReaderNo  VARCHAR2,
                                                  StaffNo   VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF Check_Reade_Book_Request(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  CASE parameter
    WHEN '"ͬ��"' THENgetBook
      -- ����黹�����
      UPDATE Book_Request
         Set Book_Request.Staff_No = StaffNo,
             Book_Request.Status   = '�ѵ���'
       WHERE Book_Request.No = ReaderNo
         AND Book_Request.ISBN = BookISBN
         AND Book_Request.Status = '������';
      -- �����߷�����Ϣ
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         'ͼ�����',
         '��ϲ������������ͼ��(ISBN:' || BookISBN || ')�Ѿ����ݣ���ӭ���ģ�',
         'δ�鿴');
    WHEN '"�ܾ�"' THEN
      -- ����黹��¼
      UPDATE Book_Request
         Set Book_Request.Staff_No = StaffNo,
             Book_Request.Status   = '����ʧ��'
       WHERE Book_Request.No = ReaderNo
         AND Book_Request.ISBN = BookISBN
         AND Book_Request.Status = '������';
      -- �����߷�����Ϣ
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         'ͼ�����',
         '��Ǹ����ΪXXX, ��������ͼ��(ISBN:' || BookISBN || ')δͨ����ˣ����Ժ�����',
         'δ�鿴');
    ELSE
      raise StatusError;
  END CASE;
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$���������Ѹ��£���ˢ�º�����$');
END DealWith_Book_Request;

--��ȡ���߼�������״̬
CREATE OR REPLACE FUNCTION query_Reader_Request_Book_History(Reader_No in Reader_Account.No%type,
                                                             pageNow   in NUMBER,
                                                             pageSize  in NUMBER,
                                                             DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Request WHERE No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''������'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Request_Book_History;

--��ȡ���߽�������״̬���ؼ���
CREATE OR REPLACE FUNCTION query_Reader_Request_Book_History_With_Keyword(Reader_No in Reader_Account.No%type,
                                                                          keyword   in VARCHAR2,
                                                                          pageNow   in NUMBER,
                                                                          pageSize  in NUMBER,
                                                                          DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Request WHERE No = ''' || Reader_No ||
           '''AND regexp_like(ISBN, ''' || keyword ||
           '+'') ORDER BY decode(Status, ''������'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Request_Book_History_With_Keyword;

--��ȡ���м�������
CREATE OR REPLACE FUNCTION get_Reader_Request_Book_History(pageNow  in NUMBER,
                                                           pageSize in NUMBER,
                                                           DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Request ORDER BY decode(Status, ''������'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Reader_Request_Book_History;

--��ȡ���м���������ؼ���
CREATE OR REPLACE FUNCTION get_Reader_Request_Book_History_With_Keyword(keyword  in VARCHAR2,
                                                                        pageNow  in NUMBER,
                                                                        pageSize in NUMBER,
                                                                        DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Request WHERE regexp_like(ISBN, ''' ||
           keyword || '+'') ORDER BY decode(Status, ''������'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Reader_Request_Book_History_With_Keyword;

-- ��ѯ�鼮ԤԼ��
CREATE OR REPLACE FUNCTION query_Book_Subscribe(BookISBN IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Book_Subscribe
   WHERE BookISBN = Book_Subscribe.Isbn
     AND Book_Subscribe.status = '��ԤԼ';
  RETURN flag;
END query_Book_Subscribe;

--��ѯ�鼮�Ĺݲ���
CREATE OR REPLACE FUNCTION query_Book_Amount(BookISBN IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Book WHERE BookISBN = Book.Isbn;
  RETURN flag;
END query_Book_Amount;


-- ��ѯ�����Ƿ��Ѿ�ԤԼ
CREATE OR REPLACE FUNCTION query_Reader_Book_Subscribe(ReaderNo IN VARCHAR2,
                                                       BookISBN IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Book_Subscribe
   WHERE BookISBN = Book_Subscribe.Isbn
     AND No = ReaderNo
     AND status = '��ԤԼ';
  RETURN flag;
END query_Reader_Book_Subscribe;

-- ����ԤԼ�鼮
CREATE OR REPLACE PROCEDURE Reader_Book_Subscribe(ReaderNo IN VARCHAR2,
                                                  BookISBN IN VARCHAR2) AS

  -- ԤԼ������
  AmountNotEnough EXCEPTION;
  -- ��ԤԼ
  ReadySubscribe EXCEPTION;
  -- �ѽ���
  ReadyBorrow EXCEPTION;
BEGIN
  IF query_Book_Amount(BookISBN) - query_Book_Subscribe(BookISBN) < 1 THEN
    raise AmountNotEnough;
  ELSIF query_Reader_Book_Subscribe(ReaderNo, BookISBN) > 0 THEN
    raise ReadySubscribe;
  ELSIF Check_Reader_Really_Borrow_Book(BookISBN, ReaderNo) > 0 THEN
    raise ReadyBorrow;
  END IF;
  INSERT INTO Book_Subscribe
  VALUES
    (ReaderNo, BookISBN, sysdate, '��ԤԼ');
EXCEPTION
  WHEN AmountNotEnough THEN
    RAISE_APPLICATION_ERROR(-20001, '$ԤԼ�������㣬���Ժ�����$');
  WHEN ReadySubscribe THEN
    RAISE_APPLICATION_ERROR(-20002, '$�����ظ�ԤԼ$');
  WHEN ReadyBorrow THEN
    RAISE_APPLICATION_ERROR(-20003, '$�ѽ��ģ�����ԤԼ$');
END Reader_Book_Subscribe;

-- ����ȡ��ԤԼ
CREATE OR REPLACE PROCEDURE Cancel_Reader_Book_Subscribe(ReaderNo IN VARCHAR2,
                                                         BookISBN IN VARCHAR2) AS
  -- ��ԤԼ
  ReadySubscribe EXCEPTION;
BEGIN
  IF query_Reader_Book_Subscribe(ReaderNo, BookISBN) < 1 THEN
    raise ReadySubscribe;
  END IF;
  UPDATE Book_Subscribe
     SET Status = '��ȡ��'
   WHERE No = ReaderNo
     AND ISBN = BookISBN
     AND Status = '��ԤԼ';
EXCEPTION
  WHEN ReadySubscribe THEN
    RAISE_APPLICATION_ERROR(-20001, '$���鼮û��ԤԼ$');
END Cancel_Reader_Book_Subscribe;

--��ѯ���ߵ�ԤԼ
CREATE OR REPLACE FUNCTION query_Reader_Book_Subscribe_History(Reader_No in Reader_Account.No%type,
                                                             pageNow   in NUMBER,
                                                             pageSize  in NUMBER,
                                                             DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Subscribe NATURAL JOIN Book WHERE No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''��ԤԼ'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Book_Subscribe_History;


--��ѯ���ߵ�ԤԼ���ؼ���
CREATE OR REPLACE FUNCTION query_Reader_Book_Subscribe_History_With_Keyword(Reader_No in Reader_Account.No%type,
                                                                          keyword   in VARCHAR2,
                                                                          pageNow   in NUMBER,
                                                                          pageSize  in NUMBER,
                                                                          DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Subscribe NATURAL JOIN Book WHERE No = ''' || Reader_No ||
           '''AND regexp_like(ISBN, ''' || keyword ||
           '+'') ORDER BY decode(Status, ''��ԤԼ'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Book_Subscribe_History_With_Keyword;

-- ����
CREATE OR REPLACE PROCEDURE reader_continue_borrow(ReaderNo IN VARCHAR2,
                                                   BookISBN IN VARCHAR2) AS
  --Ӧ�黹����
  should_date Date;
  borrow_date Date;
  -- ʱ���Ѿ�����һ����
  timeException Exception;
BEGIN
  SELECT ShouldReturnDate, borrowDate
    into should_date, borrow_date
    from READER_BORROW_RETURN_HISTORY
   WHERE No = ReaderNo
     AND ISBN = BookISBN
     AND ReturnDate is NULL;
  IF should_date < add_months(borrow_date, 2) THEN
    UPDATE READER_BORROW_RETURN_HISTORY
       SET ShouldReturnDate = add_months(ShouldReturnDate, 1)
     WHERE No = ReaderNo
       AND ISBN = BookISBN
       AND ReturnDate is NULL;
  ELSE
    raise timeException;
  END IF;
EXCEPTION
  WHEN timeException THEN
    RAISE_APPLICATION_ERROR(-20001, '$����ʧ��$');
END reader_continue_borrow;

