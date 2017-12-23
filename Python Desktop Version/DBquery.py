import MySQLdb
import tkMessageBox

class dbQuery:
    def __init__(self,dbName):
        self.dbName = dbName
        try:
            print self.dbName
            # Open database connection
            self.db = MySQLdb.connect("localhost", "root", "", dbName)
            # prepare a cursor object using cursor() method
            self.cursor = self.db.cursor()
            print self.cursor
            if self.cursor != None:
                print ("Connected")

            else:
                tkMessageBox.showinfo("!!Alert!!","Could not Connect")
        except:
            tkMessageBox.showinfo("!!Alert!!","Could not Connect...Server error")


    def retResult(self,isbnNo):
        try:
            sql = "SELECT * FROM `books` WHERE `book_id` = " + str(isbnNo)
            lst = []
            print self.cursor
            # Execute the SQL command
            print sql
            self.cursor.execute(sql)
            # Fetch all the rows in a list of lists.
            results = self.cursor.fetchall()
            for row in results:
                bookname = row[1]
                total = str(row[3])
                left = str(row[4])
                lst = [bookname,total,left]
        except:
            tkMessageBox.showinfo("!!Alert!!","Error: unable to fecth data")
        return lst


    def Lend_Return(self,isbnNo,leftNo):
        try:
            sql = "UPDATE `books` SET `pieces_at_present` = " + str(leftNo) +" WHERE `book_id` = "+str(isbnNo)
            print sql
            print self.cursor
            # Execute the SQL command
            self.cursor.execute(sql)
            # Commit your changes in the database
            self.db.commit()
        except:
            tkMessageBox.showinfo("!!Alert!!","Error: unable to fecth data in Lend")

    def Add_a_book(self,isbnNo,bookName,total):
        try:
            sql = "INSERT INTO `books`(`book_name`, `book_id`, `number_of_pieces`, `pieces_at_present`) VALUES ('"+bookName+"','"+isbnNo+"',"+total+","+total+")"
            print sql
            # Execute the SQL command
            self.cursor.execute(sql)
            # Commit your changes in the database
            self.db.commit()
        except:
            tkMessageBox.showinfo("!!Alert!!", "Error: unable to fecth data in Lend")



