--检查Admin账号是否存在
CREATE OR REPLACE FUNCTION Check_Admin_Account(Admin_No IN Admin.No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Admin WHERE Admin_No = Admin.No;
  RETURN flag;
END Check_Admin_Account;

--核对Admin账号密码
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

--检查Staff账号是否存在
CREATE OR REPLACE FUNCTION Check_Staff_Account(Staff_No IN Staff.No%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Staff WHERE Staff_No = Staff.No;
  RETURN flag;
END Check_Staff_Account;

--核对Staff账号密码
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

--检查Reader账号是否存在
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

--核对Reader账号密码
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

-- 重置Reader密码
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
    RAISE_APPLICATION_ERROR(-20001, '$账号不存在，请刷新后重试$');
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20002, '$管理员账号密码错误$');
END default_Reader_Password;

-- 重置Staff密码
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
    RAISE_APPLICATION_ERROR(-20001, '$账号不存在，请刷新后重试$');
  WHEN myExp THEN
    RAISE_APPLICATION_ERROR(-20002, '$管理员账号密码错误$');
END default_Staff_Password;

--通过ISBN核对书籍是否存在与图书管理系统中
CREATE OR REPLACE FUNCTION Check_Book_ISBN(Book_ISBN IN Book.Isbn%type)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Book WHERE Book.ISBN = Book_ISBN;
  RETURN flag;
END Check_Book_ISBN;

--通过ISBN和读者账号确认读者是否借过此书
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

--通过ISBN和读者账号确认读者是否正在申请借此书
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
     AND Staff_DealWith_Reader_Borrow_History.Status = '待处理';
  RETURN flag;
END Check_Reader_Apply_Borrow_Book;

--通过ISBN和读者账号确认读者是否在申请归还此书
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
     AND Staff_DealWith_Reader_Return_History.Status = '待处理';
  RETURN flag;
END Check_Reader_Apply_Return_Book;

--返回经过分页后的数据集 （sql, 页大小，当前是第几页，结果集）
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
  --计算v_start和v_end是多少
  v_start := v_in_pagesize * (v_in_pagenow - 1) + 1;
  v_end   := v_in_pagesize * v_in_pagenow;
  
  execute immediate 'select count(*) from (' || in_sql || ')' into DataSize;
  --执行分页
  v_sql   := 'select t2.* from (select t1.*,rownum rn from (' || in_sql ||
             ') t1 where rownum<=' || v_end || ') t2 where rn>=' || v_start;
  --DBMS_OUTPUT.PUT_LINE(v_sql);
  open res_cur for v_sql;
  RETURN res_cur;
END paging_cursor;

--查询Staff所有的操作历史记录
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END query_staff_history;

--查询Staff和ISBN有关的操作记录
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END query_staff_history_in_isbn;

--查询读者数据
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END query_reader_list;

-- 带关键字的读者查询
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

--查询Staff数据
CREATE OR REPLACE FUNCTION query_staff_list(pageNow  in NUMBER,
                                             pageSize in NUMBER,
                                             DataSize out NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Staff ORDER BY No ASC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_staff_list;

-- 带关键字的staff查询
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

--获取管理员账号详情
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END get_Admin_Details;

--获取Staff账号详情
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END get_Staff_Details;

--获取读者账号详情
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END get_Reader_Details;

--获取读者的历史纪录
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END query_reader_history;

--查询读者和ISBN有关的历史记录
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END query_reader_history_in_isbn;

-- 查询书籍类型
CREATE OR REPLACE PROCEDURE query_book_type(cur out SYS_REFCURSOR) AS
BEGIN
  open cur for
    select * from book_types;
END query_book_type;

--a)	添加图书：Staff账号，密码, ISBN，书名，作者，出版社，出版时间，类型，封面(可不需要)。
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
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
  WHEN Dup_val_on_index THEN
    RAISE_APPLICATION_ERROR(-20002, '$书籍已存在$');
  WHEN no_data_found THEN
    RAISE_APPLICATION_ERROR(-20003, '$书籍类型错误$');
END add_Book;

--b)	增加图书：ISBN，管理员账号，密码，添加数量。
CREATE OR REPLACE PROCEDURE increase_Book(Book_ISBN      Book.Isbn%type,
                                          Quantity       Book.Amount%type,
                                          Staff_Account  Staff.No%type,
                                          Staff_PassWord Staff.PassWord%type) AS
  -- 没有找到书籍异常
  NoBook EXCEPTION;
  -- 数量为负数异常
  NonNegative EXCEPTION;
  -- 账号密码错误异常
  AccPasErr EXCEPTION;
BEGIN
  IF Check_Staff(Staff_Account, Staff_PassWord) = 1 THEN
    IF Check_Book_ISBN(Book_ISBN) = 0 THEN
      raise NoBook;
    ELSIF Quantity < 0 THEN
      raise NonNegative;
    ELSE
      --更新书籍信息
      UPDATE Book
         SET Book.Amount    = Book.Amount + Quantity,
             Book.Available = Book.Available + Quantity
       WHERE Book.Isbn = Book_ISBN;
      -- 添加管理员—书籍表
      INSERT INTO Staff_DealWith_Book_History
      VALUES
        (Staff_Account, Book_ISBN, sysdate, Quantity);
    END IF;
  ELSE
    raise AccPasErr;
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20003, '$没有找到此书籍$');
  WHEN NonNegative THEN
    RAISE_APPLICATION_ERROR(-20004, '$数量不能为负数$');
  WHEN AccPasErr THEN
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END increase_Book;

--c)	删除图书：ISBN，Staff账号，密码，删除数量。
CREATE OR REPLACE PROCEDURE decrease_Book(Book_ISBN      Book.Isbn%type,
                                          Quantity       Book.Amount%type,
                                          Staff_Account  Staff.No%type,
                                          Staff_PassWord Staff.PassWord%type) AS
  Book_Available number;
  -- 没有找到书籍异常
  NoBook EXCEPTION;
  -- 数量为负数异常
  NonNegative EXCEPTION;
  -- 减少数量超出限制
  AmountLimit EXCEPTION;
  -- 账号密码错误异常
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
        --更新书籍信息
        UPDATE Book
           SET Book.Amount    = Book.Amount - Quantity,
               Book.Available = Book.Available - Quantity
         WHERE Book.Isbn = Book_ISBN;
        -- 更新管理员—书籍表
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
    RAISE_APPLICATION_ERROR(-20003, '$没有找到此书籍$');
  WHEN NonNegative THEN
    RAISE_APPLICATION_ERROR(-20004, '$数量不能为负数$');
  WHEN AccPasErr THEN
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
  WHEN AmountLimit THEN
    RAISE_APPLICATION_ERROR(-20005, '$减少数量超出限制$');
END decrease_Book;

--d)  修改图书信息：图书的属性，当前登录账号，密码。
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
  -- 没有找到书籍异常
  NoBook EXCEPTION;
  -- 账号密码错误异常
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
    RAISE_APPLICATION_ERROR(-20003, '$没有找到此书籍$');
  WHEN AccPasErr THEN
    RAISE_APPLICATION_ERROR(-20001, '$账号或密码错误$');
END mod_Book;

--e)	注册账号：注册账号所需要的数据。
--管理员
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
    RAISE_APPLICATION_ERROR(-20002, '$账号已存在$');
END register_Account_Of_Admin;
--读者
CREATE OR REPLACE PROCEDURE register_Account_Of_Reader(Reader_No       Reader_Account.No%type,
                                                       Reader_Password Reader_Account.Password%type,
                                                       Reader_Name     Reader_Details.Name%type,
                                                       Reader_Gender   Reader_Details.Gender%type,
                                                       Reader_Fine     Reader_Details.Fine%type) AS
BEGIN
  --先将账号密码存入
  INSERT INTO Reader_Account VALUES (Reader_No, Reader_Password);
  --再存入个人资料
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
    RAISE_APPLICATION_ERROR(-20002, '$账号已存在$');
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
    RAISE_APPLICATION_ERROR(-20002, '$账号已存在$');
END register_Account_Of_Staff;

--f)	修改账号信息
--管理员
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

--读者
CREATE OR REPLACE PROCEDURE mod_Account_Of_Reader(Reader_No       Reader_Account.No%type,
                                                  Reader_Password Reader_Account.Password%type,
                                                  Reader_Name     Reader_Details.Name%type,
                                                  Reader_Gender   Reader_Details.Gender%type,
                                                  Reader_Fine     Reader_Details.Fine%type) AS
BEGIN
  --先修改密码
  UPDATE Reader_Account
     SET Password = Reader_Password
   WHERE No = Reader_No;
  --再修改个人资料
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

--g)	删除账号：当前登录账号，密码。
--管理员
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
    RAISE_APPLICATION_ERROR(-20001, '$账号不存在$');
END del_Account_Of_Admin;

--读者
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
    RAISE_APPLICATION_ERROR(-20001, '$账号不存在$');
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
    RAISE_APPLICATION_ERROR(-20001, '$账号不存在$');
END del_Account_Of_Staff;

--h)	图书查询：图书的部分属性的组合。
--任意词模糊查询
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

--关键词模糊查询
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
      DBMS_OUTPUT.PUT_LINE('关键词错误');
  END CASE;
END query_key_word;

--i)	借阅图书：ISBN，读者账号。
CREATE OR REPLACE PROCEDURE Borrow_Book(Book_ISBN Book.isbn%type,
                                        Reader_No Reader_Account.No%type) AS
  -- 没有找到书籍异常
  NoBook EXCEPTION;
  -- 重复借书异常
  TwiceBorrowErr EXCEPTION;
  -- 馆藏数不足
  AmountLimit EXCEPTION;
  -- 重复申请异常
  RePlayApply EXCEPTION;
  -- 罚款异常
  ExistFine EXCEPTION;
  -- 剩余馆藏数
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
        (Reader_No, Book_ISBN, sysdate, null, '待处理');
    END IF;
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20003, '$没有找到此书籍$');
  WHEN TwiceBorrowErr THEN
    RAISE_APPLICATION_ERROR(-20006, '$不能同时借阅同一书籍$');
  WHEN AmountLimit THEN
    RAISE_APPLICATION_ERROR(-20005, '$馆藏数不足$');
  WHEN RePlayApply THEN
    RAISE_APPLICATION_ERROR(-20002, '$您已经申请借阅，请不要重复申请$');
  WHEN ExistFine THEN
    RAISE_APPLICATION_ERROR(-20007, '$存在罚款未缴纳，请缴纳罚款后再试$');
END Borrow_Book;

--j)	归还图书：ISBN，读者账号，密码。
CREATE OR REPLACE PROCEDURE Return_Book(Book_ISBN Book.isbn%type,
                                        Reader_No Reader_Account.No%type) AS
  -- 没有找到书籍异常
  NoBook EXCEPTION;
  -- 重复申请异常
  RePlayApply EXCEPTION;
BEGIN
  IF Check_Reader_Really_Borrow_Book(Book_ISBN, Reader_No) = 0 THEN
    raise NoBook;
  ELSIF Check_Reader_Apply_Return_Book(Book_ISBN, Reader_No) >= 1 THEN
    raise RePlayApply;
  ELSE
    -- 插入管理员归还处理列表
    INSERT INTO Staff_DealWith_Reader_Return_History
    VALUES
      (Reader_No, Book_ISBN, sysdate, null, '待处理');
  END IF;
EXCEPTION
  WHEN NoBook THEN
    RAISE_APPLICATION_ERROR(-20001,
                            '$此书不在图书馆或未在账号中找到此书的借阅记录$');
  WHEN RePlayApply THEN
    RAISE_APPLICATION_ERROR(-20002, '$请不要重复申请$');
END Return_Book;

--Staff处理借阅图书
CREATE OR REPLACE PROCEDURE DealWith_Borrow_Book(parameter VARCHAR2,
                                                 BookISBN Book.isbn%type,
                                                 ReaderNo Reader_Account.No%type,
                                                 StaffNo  Staff_DealWith_Reader_Borrow_History.Staff_No%type) AS
  -- 罚款异常
  ExistFine EXCEPTION;
  -- 应交罚款
  ShouldPayFine NUMBER;
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF check_reader_apply_borrow_book(BookISBN, ReaderNo) = 0 THEN
     raise StatusError;
  END IF;
  CASE parameter
    WHEN '"同意"' THEN
      -- 检查是否存在罚款
      Select Fine
        INTO ShouldPayFine
        From Reader_Details
       WHERE ReaderNo = No;
      IF ShouldPayFine > 0 THEN
        raise ExistFine;
      END IF;
      -- 更新借阅申请记录
      UPDATE Staff_DealWith_Reader_Borrow_History
         Set Staff_DealWith_Reader_Borrow_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Borrow_History.Status   = '申请成功'
       WHERE Staff_DealWith_Reader_Borrow_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Borrow_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Borrow_History.Status = '待处理';
      -- 更新读者借阅记录
      INSERT INTO Reader_Borrow_Return_History
      Values
        (ReaderNo, BookISBN, sysdate, add_months(sysdate, 1), null);
      -- 给读者发消息
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '借阅申请',
         '恭喜您，您的借书申请(ISBN:' || BookISBN || ')已经审核通过，您的书籍将会放置在XXX处，请自行取书！',
         '未查看');
    WHEN '"拒绝"' THEN
      -- 更新借阅申请记录
      UPDATE Staff_DealWith_Reader_Borrow_History
         Set Staff_DealWith_Reader_Borrow_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Borrow_History.Status   = '申请失败'
       WHERE Staff_DealWith_Reader_Borrow_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Borrow_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Borrow_History.Status = '待处理';
      -- 将书籍的数量加起来
      UPDATE Book
         Set Book.Available = Book.Available + 1
       Where BookISBN = Book.Isbn;
      -- 给读者发送消息
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '借阅申请',
         '抱歉，因为XXX, 您的借书申请(ISBN:' || BookISBN || ')未通过审核！请稍后重试',
         '未查看');
    ELSE
      raise StatusError;
  END CASE;
EXCEPTION
  WHEN ExistFine THEN
    RAISE_APPLICATION_ERROR(-20007, '$存在罚款未缴纳，请缴纳罚款后再试$');
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$书籍状态已更新，请刷新后重试$');
END DealWith_Borrow_Book;
    
--Staff处理归还图书
CREATE OR REPLACE PROCEDURE DealWith_Return_Book(parameter VARCHAR2,
                                                 BookISBN  VARCHAR2,
                                                 ReaderNo  VARCHAR2,
                                                 StaffNo   VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
  -- Time_Day
  Days        INT;
  should_Date Date;
  -- 读者NO
  SubReaderNo VARCHAR2(50);
BEGIN
  IF check_reader_apply_return_book(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  CASE parameter
    WHEN '"同意"' THEN
      -- 处理归还申请表
      UPDATE Staff_DealWith_Reader_Return_History
         Set Staff_DealWith_Reader_Return_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Return_History.Status   = '申请成功'
       WHERE Staff_DealWith_Reader_Return_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Return_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Return_History.Status = '待处理';
      --处理罚款
      --计算天数
      Select Shouldreturndate
        INTO should_Date
        From Reader_Borrow_Return_History
       WHERE No = ReaderNo
         AND ISBN = BookISBN
         AND ReturnDate IS NULL;
      Days := TO_NUMBER(sysdate - should_date);
      DBMS_OUTPUT.PUT_LINE(Days);
      --如果天数大于30天
      IF Days >= 30 THEN
        UPDATE Reader_Details
           SET Fine = Fine + (Days - 29) * 0.6
         WHERE ReaderNo = No;
      END IF;
      -- 更新读者借阅表
      UPDATE Reader_Borrow_Return_History
         Set ReturnDate = sysdate
       WHERE No = ReaderNo
         AND ISBN = BookISBN
         AND ReturnDate is null;
      -- 给读者发送消息
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '归还申请',
         '恭喜您，您的还书申请(ISBN:' || BookISBN ||
         ')已经审核通过，请在xxx时间内将书放置到xxx柜台处，会有工作人员为您处理！',
         '未查看');
      -- 将书籍的数量加起来
      UPDATE Book
         Set Book.Available = Book.Available + 1
       Where BookISBN = Book.Isbn;
      -- 将书籍给予预约的读者
      IF query_Book_Subscribe(BookISBN) > 0 THEN
        -- 查询第一个预约的读者
        select No
          INTO SubReaderNo
          from (select *
                  from Book_Subscribe
                 WHERE ISBN = BookISBN
                   AND Status = '已预约'
                 order by Time asc)
         where rownum = 1;
        -- 更新预约表
        UPDATE Book_Subscribe
           SET Status = '已完成'
         WHERE SubReaderNo = No
           AND ISBN = BookISBN
           AND Status = '已预约';
        -- 将书籍加入预约的读者借阅表
        INSERT INTO Reader_Borrow_Return_History
        Values
          (SubReaderNo, BookISBN, sysdate, add_months(sysdate, 1), null);
      END IF;
    WHEN '"拒绝"' THEN
      -- 处理归还记录
      UPDATE Staff_DealWith_Reader_Return_History
         Set Staff_DealWith_Reader_Return_History.Staff_No = StaffNo,
             Staff_DealWith_Reader_Return_History.Status   = '申请失败'
       WHERE Staff_DealWith_Reader_Return_History.Reader_No = ReaderNo
         AND Staff_DealWith_Reader_Return_History.ISBN = BookISBN
         AND Staff_DealWith_Reader_Return_History.Status = '待处理';
      -- 给读者发送消息
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '归还申请',
         '抱歉，因为XXX, 您的还书申请(ISBN:' || BookISBN || ')未通过审核！请稍后重试',
         '未查看');
    ELSE
      raise StatusError;
  END CASE;
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$书籍状态已更新，请刷新后重试$');
END DealWith_Return_Book;

--查询读者借书申请状态
CREATE OR REPLACE FUNCTION query_Staff_DealWith_Reader_Borrow_History(Reader_No in Reader_Account.No%type,
                                                                      pageNow   in NUMBER,
                                                                      pageSize  in NUMBER,
                                                                      DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_BORROW_HISTORY NATURAL JOIN Book ' ||
           'WHERE Reader_No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Borrow_History;

--查询读者借书申请状态带关键字
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
           '+'') ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);   
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Borrow_History_With_Keyword;

--查询读者还书申请
CREATE OR REPLACE FUNCTION query_Staff_DealWith_Reader_Return_History(Reader_No in Reader_Account.No%type,
                                                                      pageNow   in NUMBER,
                                                                      pageSize  in NUMBER,
                                                                      DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_RETURN_HISTORY NATURAL JOIN Book ' ||
           'WHERE Reader_No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Return_History;

--查询读者还书申请带关键字
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
           '+'') ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Staff_DealWith_Reader_Return_History_With_Keyword;


--获取所有的还书申请
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Return_History(pageNow  in NUMBER,
                                                                    pageSize in NUMBER,
                                                                    DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_RETURN_HISTORY NATURAL JOIN Book ' ||
           'ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Return_History;

--获取所有的还书申请带关键字
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Return_History_With_Keyword(keyword  in VARCHAR2,
                                                                                 pageNow  in NUMBER,
                                                                                 pageSize in NUMBER,
                                                                                 DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_RETURN_HISTORY NATURAL JOIN Book ' ||
           'WHERE regexp_like(Reader_No, ''' || keyword ||
           '+'') ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Return_History_With_Keyword;


--获取所有借书申请状态
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Borrow_History(pageNow  in NUMBER,
                                                                    pageSize in NUMBER,
                                                                    DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_BORROW_HISTORY NATURAL JOIN Book ' ||
           'ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Borrow_History;

--查询所有借书申请状态带关键字
CREATE OR REPLACE FUNCTION get_Staff_DealWith_Reader_Borrow_History_With_Keyword(keyword  in VARCHAR2,
                                                                                 pageNow  in NUMBER,
                                                                                 pageSize in NUMBER,
                                                                                 DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT Reader_No, Name, ISBN, Cover, Time, Staff_no, Status FROM STAFF_DEALWITH_READER_BORROW_HISTORY NATURAL JOIN Book ' ||
           'WHERE regexp_like(ISBN, ''' || keyword ||
           '+'') ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Staff_DealWith_Reader_Borrow_History_With_Keyword;


-- 获取读者消息
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

-- 设置消息已读
CREATE OR REPLACE FUNCTION set_Reader_Message(Reader_No in Reader_Account.No%type,
                                              t         in Date)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  UPDATE Reader_Message
     SET Status = '已读'
   WHERE No = Reader_No
     AND Time = t
     AND Status = '未查看';
END set_Reader_Message;

-- 取消借阅申请
CREATE OR REPLACE PROCEDURE cancel_Reader_Borrow_Apply(BookISBN VARCHAR2,
                                                       ReaderNo VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF check_reader_apply_borrow_book(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  -- 更新借阅申请记录
  UPDATE Staff_DealWith_Reader_Borrow_History
     Set Staff_DealWith_Reader_Borrow_History.Status = '已取消'
   WHERE Staff_DealWith_Reader_Borrow_History.Reader_No = ReaderNo
     AND Staff_DealWith_Reader_Borrow_History.ISBN = BookISBN
     AND Staff_DealWith_Reader_Borrow_History.Status = '待处理';
  -- 将书籍的数量加起来
  UPDATE Book
     Set Book.Available = Book.Available + 1
   Where BookISBN = Book.Isbn;
  -- 给读者发送消息
  INSERT INTO Reader_Message
  VALUES
    (ReaderNo,
     sysdate,
     '借阅申请',
     '您的借书申请(ISBN:' || BookISBN || ')已成功取消，欢迎您下次借阅！',
     '未查看');
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$书籍状态已更新，请刷新后重试$');
END cancel_Reader_Borrow_Apply;

--取消归还申请
CREATE OR REPLACE PROCEDURE cancel_Reader_Return_Apply(BookISBN VARCHAR2,
                                                       ReaderNo VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF check_reader_apply_return_book(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  -- 更新借阅申请记录
  UPDATE Staff_DealWith_Reader_Return_History
     Set Staff_DealWith_Reader_Return_History.Status = '已取消'
   WHERE Staff_DealWith_Reader_Return_History.Reader_No = ReaderNo
     AND Staff_DealWith_Reader_Return_History.ISBN = BookISBN
     AND Staff_DealWith_Reader_Return_History.Status = '待处理';
  -- 给读者发送消息
  INSERT INTO Reader_Message
  VALUES
    (ReaderNo,
     sysdate,
     '还书申请',
     '您的还书申请(ISBN:' || BookISBN || ')已成功取消！',
     '未查看');
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$书籍状态已更新，请刷新后重试$');
END cancel_Reader_Return_Apply;

-- 查询图书是否正在被荐购
CREATE OR REPLACE FUNCTION Check_Book_Request(BookISBN IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Book_Request
   WHERE BookISBN = Book_Request.Isbn
     AND Book_Request.status = '待处理';
  RETURN flag;
END Check_Book_Request;

-- 查询图书是否正在被某读者荐购
CREATE OR REPLACE FUNCTION Check_Reade_Book_Request(BookISBN IN VARCHAR2,
                                                    ReaderNo IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Book_Request
   WHERE BookISBN = Book_Request.Isbn
     AND Book_Request.status = '待处理'
     AND No = ReaderNo;
  RETURN flag;
END Check_Reade_Book_Request;

--Reader荐购图书
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
     '待处理',
     null);
EXCEPTION
  WHEN BookExist THEN
    RAISE_APPLICATION_ERROR(-20001, '$书籍已存在，无法荐购$');
  WHEN BookReady THEN
    RAISE_APPLICATION_ERROR(-20002, '$管理员正在审核此书籍，请稍后再试$');
  WHEN ReaderBook THEN
    RAISE_APPLICATION_ERROR(-20002,
                            '$您的荐购申请正在审核中，请不要重复荐购$');
END reader_book_request;

--读者取消荐购申请
CREATE OR REPLACE PROCEDURE cancel_Book_Request(BookISBN VARCHAR2,
                                                ReaderNo VARCHAR2) AS
  -- StatusError
  StatusError EXCEPTION;
BEGIN
  IF Check_Reade_Book_Request(BookISBN, ReaderNo) = 0 THEN
    raise StatusError;
  END IF;
  -- 更新借阅申请记录
  UPDATE Book_Request
     Set Book_Request.Status = '已取消'
   WHERE Book_Request.No = ReaderNo
     AND Book_Request.ISBN = BookISBN
     AND Book_Request.Status = '待处理';
  -- 给读者发送消息
  INSERT INTO Reader_Message
  VALUES
    (ReaderNo,
     sysdate,
     '荐购申请',
     '您荐购图书申请(ISBN:' || BookISBN || ')已成功取消！',
     '未查看');
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$书籍状态已更新，请刷新后重试$');
END cancel_Book_Request;

-- Staff处理荐购申请
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
    WHEN '"同意"' THENgetBook
      -- 处理归还申请表
      UPDATE Book_Request
         Set Book_Request.Staff_No = StaffNo,
             Book_Request.Status   = '已到馆'
       WHERE Book_Request.No = ReaderNo
         AND Book_Request.ISBN = BookISBN
         AND Book_Request.Status = '待处理';
      -- 给读者发送消息
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '图书荐购',
         '恭喜您，您荐购的图书(ISBN:' || BookISBN || ')已经到馆，欢迎借阅！',
         '未查看');
    WHEN '"拒绝"' THEN
      -- 处理归还记录
      UPDATE Book_Request
         Set Book_Request.Staff_No = StaffNo,
             Book_Request.Status   = '荐购失败'
       WHERE Book_Request.No = ReaderNo
         AND Book_Request.ISBN = BookISBN
         AND Book_Request.Status = '待处理';
      -- 给读者发送消息
      INSERT INTO Reader_Message
      VALUES
        (ReaderNo,
         sysdate,
         '图书荐购',
         '抱歉，因为XXX, 您荐购的图书(ISBN:' || BookISBN || ')未通过审核！请稍后重试',
         '未查看');
    ELSE
      raise StatusError;
  END CASE;
EXCEPTION
  WHEN StatusError THEN
    RAISE_APPLICATION_ERROR(-20001, '$荐购申请已更新，请刷新后重试$');
END DealWith_Book_Request;

--获取读者荐购申请状态
CREATE OR REPLACE FUNCTION query_Reader_Request_Book_History(Reader_No in Reader_Account.No%type,
                                                             pageNow   in NUMBER,
                                                             pageSize  in NUMBER,
                                                             DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Request WHERE No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Request_Book_History;

--获取读者借书申请状态带关键字
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
           '+'') ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Request_Book_History_With_Keyword;

--获取所有荐购申请
CREATE OR REPLACE FUNCTION get_Reader_Request_Book_History(pageNow  in NUMBER,
                                                           pageSize in NUMBER,
                                                           DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Request ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  --DBMS_OUTPUT.PUT_LINE(insql);
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Reader_Request_Book_History;

--获取所有荐购申请带关键字
CREATE OR REPLACE FUNCTION get_Reader_Request_Book_History_With_Keyword(keyword  in VARCHAR2,
                                                                        pageNow  in NUMBER,
                                                                        pageSize in NUMBER,
                                                                        DataSize OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Request WHERE regexp_like(ISBN, ''' ||
           keyword || '+'') ORDER BY decode(Status, ''待处理'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END get_Reader_Request_Book_History_With_Keyword;

-- 查询书籍预约量
CREATE OR REPLACE FUNCTION query_Book_Subscribe(BookISBN IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*)
    INTO flag
    FROM Book_Subscribe
   WHERE BookISBN = Book_Subscribe.Isbn
     AND Book_Subscribe.status = '已预约';
  RETURN flag;
END query_Book_Subscribe;

--查询书籍的馆藏量
CREATE OR REPLACE FUNCTION query_Book_Amount(BookISBN IN VARCHAR2)
  RETURN NUMBER IS
  flag number;
BEGIN
  SELECT count(*) INTO flag FROM Book WHERE BookISBN = Book.Isbn;
  RETURN flag;
END query_Book_Amount;


-- 查询读者是否已经预约
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
     AND status = '已预约';
  RETURN flag;
END query_Reader_Book_Subscribe;

-- 读者预约书籍
CREATE OR REPLACE PROCEDURE Reader_Book_Subscribe(ReaderNo IN VARCHAR2,
                                                  BookISBN IN VARCHAR2) AS

  -- 预约量不够
  AmountNotEnough EXCEPTION;
  -- 已预约
  ReadySubscribe EXCEPTION;
  -- 已借阅
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
    (ReaderNo, BookISBN, sysdate, '已预约');
EXCEPTION
  WHEN AmountNotEnough THEN
    RAISE_APPLICATION_ERROR(-20001, '$预约数量不足，请稍后再试$');
  WHEN ReadySubscribe THEN
    RAISE_APPLICATION_ERROR(-20002, '$请勿重复预约$');
  WHEN ReadyBorrow THEN
    RAISE_APPLICATION_ERROR(-20003, '$已借阅，不能预约$');
END Reader_Book_Subscribe;

-- 读者取消预约
CREATE OR REPLACE PROCEDURE Cancel_Reader_Book_Subscribe(ReaderNo IN VARCHAR2,
                                                         BookISBN IN VARCHAR2) AS
  -- 已预约
  ReadySubscribe EXCEPTION;
BEGIN
  IF query_Reader_Book_Subscribe(ReaderNo, BookISBN) < 1 THEN
    raise ReadySubscribe;
  END IF;
  UPDATE Book_Subscribe
     SET Status = '已取消'
   WHERE No = ReaderNo
     AND ISBN = BookISBN
     AND Status = '已预约';
EXCEPTION
  WHEN ReadySubscribe THEN
    RAISE_APPLICATION_ERROR(-20001, '$此书籍没有预约$');
END Cancel_Reader_Book_Subscribe;

--查询读者的预约
CREATE OR REPLACE FUNCTION query_Reader_Book_Subscribe_History(Reader_No in Reader_Account.No%type,
                                                             pageNow   in NUMBER,
                                                             pageSize  in NUMBER,
                                                             DataSize  OUT NUMBER)
  RETURN SYS_REFCURSOR IS
  insql VARCHAR2(1000);
BEGIN
  insql := 'SELECT * FROM Book_Subscribe NATURAL JOIN Book WHERE No = ''' || Reader_No ||
           ''' ORDER BY decode(Status, ''已预约'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Book_Subscribe_History;


--查询读者的预约带关键字
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
           '+'') ORDER BY decode(Status, ''已预约'', 1), Time DESC';
  RETURN paging_cursor(insql, pageSize, pageNow, DataSize);
END query_Reader_Book_Subscribe_History_With_Keyword;

-- 续借
CREATE OR REPLACE PROCEDURE reader_continue_borrow(ReaderNo IN VARCHAR2,
                                                   BookISBN IN VARCHAR2) AS
  --应归还日期
  should_date Date;
  borrow_date Date;
  -- 时间已经加了一个月
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
    RAISE_APPLICATION_ERROR(-20001, '$续借失败$');
END reader_continue_borrow;

