Create Table Book_Types(
	Type_No Int Primary Key,
	Type VARCHAR2(50) Not Null
);

Create Table Reader_Account( 
	No VARCHAR2(50) Primary Key,
	Password VARCHAR2(20) Not Null
);

Create Table Reader_Details( 
	No VARCHAR2(50) Primary Key,
	Name VARCHAR2(100) Not Null,
	Gender VARCHAR2(5) Check(Gender IN ('男','女')),
	Fine NUMBER(*,2),
	TakeEffectDate Date DEFAULT sysdate,
	LoseEffectDate Date,
	Foreign Key (No) References Reader_Account(No) ON DELETE CASCADE
);

Create Table Admin
( 
	No VARCHAR2(50) Primary Key,
	Password VARCHAR2(20) Not Null,
	Name VARCHAR2(100) Not Null,
	Gender VARCHAR2(5) Check(Gender IN ('男','女')),
	Phone VARCHAR2(13)
);

Create Table Staff
( 
	No VARCHAR2(50) Primary Key,
	Password VARCHAR2(20) Not Null,
	Name VARCHAR2(100) Not Null,
	Gender VARCHAR2(5) Check(Gender IN ('男','女')),
	Phone VARCHAR2(13)
);

Create Table Book( 
	ISBN VARCHAR2(20) Primary Key,
	Name VARCHAR2(50) Not Null,
	Author VARCHAR2(50) Not Null,
	Type_No Int,
	Publisher VARCHAR2(100) Not null,
	PublishDate Date Not null,
	Value NUMBER,
	Amount Int,
	Available Int,
	Cover VARCHAR2(100),
	Foreign Key (Type_No) References Book_Types(Type_No)
);

Create Table Staff_DealWith_Book_History( 
	No VARCHAR2(50) Not Null,
	ISBN VARCHAR2(20) Not Null,
	PurchaseDate Date Not Null,
	PurchaseAmount Int Not Null,
	Foreign Key (ISBN) References Book(ISBN) ON DELETE CASCADE,
	Foreign Key (No) References Staff(No) ON DELETE CASCADE
);

Create Table Staff_DealWith_Reader_Borrow_History( 
	Reader_No VARCHAR2(50) Not Null,
	ISBN VARCHAR2(20) Not Null,
	Time Date Not Null,
	Staff_No VARCHAR2(50),
	Status VARCHAR2(50) Not Null,
	Foreign Key (ISBN) References Book(ISBN) ON DELETE CASCADE,
	Foreign Key (Staff_No) References Staff(No) ON DELETE CASCADE,
	Foreign Key (Reader_No) References Reader_Account(No) ON DELETE CASCADE
);

Create Table Staff_DealWith_Reader_Return_History( 
	Reader_No VARCHAR2(50) Not Null,
	ISBN VARCHAR2(20) Not Null,
	Time Date Not Null,
	Staff_No VARCHAR2(50),
	Status VARCHAR2(50) Not Null,
	Foreign Key (ISBN) References Book(ISBN) ON DELETE CASCADE,
	Foreign Key (Staff_No) References Staff(No) ON DELETE CASCADE,
	Foreign Key (Reader_No) References Reader_Account(No) ON DELETE CASCADE
);

Create Table Reader_Borrow_Return_History( 
	No VARCHAR2(50) Not Null,
	ISBN VARCHAR2(20) Not Null,
	BorrowDate Date Not Null,
	ShouldReturnDate Date Not Null,
	ReturnDate Date,
	Foreign Key (ISBN) References Book(ISBN) ON DELETE CASCADE,
	Foreign Key (No) References Reader_Account(No) ON DELETE CASCADE
);

Create Table Book_Request( 
	No VARCHAR2(50) Not Null,
	ISBN VARCHAR2(20) Not Null,
	Name VARCHAR2(50) Not Null,
	Author VARCHAR2(50) ,
	Publisher VARCHAR2(100) ,
	PublishDate Date ,
	Time Date Not Null,
	Status VARCHAR2(50) Not Null,
	Staff_No VARCHAR2(50),
	Foreign Key (No) References Reader_Account(No) ON DELETE CASCADE,
	Foreign Key (Staff_No) References Staff(No) ON DELETE CASCADE
);

Create Table Book_Subscribe( 
	No VARCHAR2(50) Not Null,
	ISBN VARCHAR2(20) Not Null,
	Time Date Not Null,
	Status VARCHAR2(50) Not Null,
	Foreign Key (ISBN) References Book(ISBN) ON DELETE CASCADE,
	Foreign Key (No) References Reader_Account(No) ON DELETE CASCADE
);
