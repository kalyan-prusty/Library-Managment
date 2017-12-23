import Tkinter as tk
import DBquery
import tkMessageBox

root = tk.Tk()

def submit(currentPage):
    status.config(text="")
    bookLabel.config(text = "")
    totalLabel.config(text = "")
    leftLabel.config(text = "")
    if currentPage == "Main":
        try:
            isbnNo = isbnLabel.get()
            if isbnNo.strip() != "":
                connection = DBquery.dbQuery("library")
                lst = connection.retResult(isbnNo)
                if len(lst) > 0:
                    bookLabel.config(text = lst[0])
                    totalLabel.config(text = lst[1])
                    leftLabel.config(text = lst[2])
                else:
                    tkMessageBox.showinfo("!!Alert!!","Error in fetching from Database.Check ISBN number")
            else:
                tkMessageBox.showinfo("!!Alert!!","Please fill the requirments")
        except:
            tkMessageBox.showinfo("!!Alert!!","Error in fetching from Database")

    elif currentPage == "Lend":
        try:
            isbnNo = isbnLabel.get()
            if isbnNo.strip() != "":
                connection = DBquery.dbQuery("library")
                lst = connection.retResult(isbnNo)
                if len(lst) > 0:
                    bookLabel.config(text = lst[0])
                    left = int(lst[2]) - 1
                    leftLabel.config(text = left)
                    connection.Lend_Return(isbnNo,left)
                    status.config(text = "Lending Successful...")
                else:
                    tkMessageBox.showinfo("!!Alert!!","Error in fetching from Database.Check ISBN number")
            else:
                tkMessageBox.showinfo("!!Alert!!","Please fill the requirments")
        except:
            tkMessageBox.showinfo("!!Alert!!","Error in fetching from Database")

    elif currentPage == "Return":
        try:
            isbnNo = isbnLabel.get()
            if isbnNo.strip() != "":
                connection = DBquery.dbQuery("library")
                lst = connection.retResult(isbnNo)
                if len(lst) > 0:
                    bookLabel.config(text = lst[0])
                    left = int(lst[2]) + 1
                    leftLabel.config(text = left)
                    connection.Lend_Return(isbnNo,left)
                    status.config(text="Returning Successful...")
                else:
                    tkMessageBox.showinfo("!!Alert!!","Error in fetching from Database.Check ISBN number")
            else:
                tkMessageBox.showinfo("!!Alert!!","Please fill the requirments")
        except:
            tkMessageBox.showinfo("!!Alert!!","Error in fetching from Database")

    elif currentPage == "Add_a_book":
        try:
            isbnNo = isbnLabel.get()
            bookName = bookEntry.get()
            totalBooks = totalEntry.get()
            if isbnNo.strip() != "" and bookName != "" and totalBooks != "":
                connection = DBquery.dbQuery("library")
                isbnLabel.delete(0, tk.END)
                bookEntry.delete(0, tk.END)
                totalEntry.delete(0, tk.END)
                connection.Add_a_book(isbnNo,bookName,totalBooks)
                status.config(text="Added a book Successfully...")
            else:
                tkMessageBox.showinfo("!!Alert!!","Please fill the requirments")
        except:
            tkMessageBox.showinfo("!!Alert!!","Error in fetching from Database")

frameMenu = tk.Frame(root, bd=3, relief=tk.SUNKEN,width = 350,height = 30,bg = "yellow")
frameMenu.grid_propagate(0)
frameMenu.pack()

frame = tk.Frame(root,width = 350,height = 230)
frame.grid_propagate(0)
frame.pack()

isbnTextLabel = tk.Label(frame,text = "ISBN* :",font=("Courier", 20,"bold"))
isbnLabel = tk.Entry(frame,font = ("Times",17))
bookTextLabel = tk.Label(frame,text = "Book name :",font=("Courier", 20,"bold"))
bookLabel = tk.Label(frame,text = "N/A",font = ("Times",17))
totalTextLabel = tk.Label(frame,text = "Total books:",font=("Courier", 20,"bold"))
totalLabel = tk.Label(frame,text = "N/A",font = ("Times",17))
leftTextLabel = tk.Label(frame,text = "Pieces at \npresent:",font=("Courier", 20,"bold"))
leftLabel = tk.Label(frame,text = "N/A",font = ("Times",17))

bookEntry = tk.Entry(frame,font = ("Times",17))
totalEntry = tk.Entry(frame,font = ("Times",17))

status = tk.Label(root, text="", bd=3, relief=tk.SUNKEN, anchor=tk.W)
status.pack(side=tk.BOTTOM, fill=tk.X)

def Lend():
    bookEntry.grid_forget()
    totalEntry.grid_forget()
    root.title("Lend a book")
    isbnTextLabel.grid(row = 1, column = 0, sticky = tk.E)
    isbnLabel.grid(row = 1,column = 1,sticky = tk.W)
    bookTextLabel.grid(row = 2,column = 0,sticky = tk.E)
    bookLabel.grid(row = 2,column = 1,sticky = tk.W)
    totalTextLabel.grid_forget()
    totalLabel.grid_forget()
    leftTextLabel.grid(row = 4,column = 0,sticky = tk.E)
    leftLabel.grid(row = 4,column = 1,sticky = tk.W)
    button = tk.Button(frame,text = "Submit",command= lambda:submit("Lend"),bg = "red",fg = "yellow")
    button.grid(row = 5,columnspan = 2)

    status.config(text="")
    bookLabel.config(text="")
    totalLabel.config(text="")
    leftLabel.config(text="")

def Return():
    bookEntry.grid_forget()
    totalEntry.grid_forget()
    root.title("Return a book")
    isbnTextLabel.grid(row=1, column=0, sticky=tk.E)
    isbnLabel.grid(row=1, column=1, sticky=tk.W)
    bookTextLabel.grid(row=2, column=0, sticky=tk.E)
    bookLabel.grid(row=2, column=1, sticky=tk.W)
    totalTextLabel.grid_forget()
    totalLabel.grid_forget()
    leftTextLabel.grid(row=4, column=0, sticky=tk.E)
    leftLabel.grid(row=4, column=1, sticky=tk.W)
    button = tk.Button(frame, text="Submit", command=lambda: submit("Return"), bg="red", fg="yellow")
    button.grid(row=5, columnspan=2)

    status.config(text="")
    bookLabel.config(text="")
    totalLabel.config(text="")
    leftLabel.config(text="")

def Main():
    bookEntry.grid_forget()
    totalEntry.grid_forget()
    root.title("Library")
    isbnTextLabel.grid(row = 1, column = 0, sticky = tk.E)
    isbnLabel.grid(row = 1,column = 1,sticky = tk.W)
    bookTextLabel.grid(row = 2,column = 0,sticky = tk.E)
    bookLabel.grid(row = 2,column = 1,sticky = tk.W)
    totalTextLabel.grid(row = 3,column = 0,sticky = tk.E)
    totalLabel.grid(row = 3,column = 1,sticky = tk.W)
    leftTextLabel.grid(row = 4,column = 0,sticky = tk.E)
    leftLabel.grid(row = 4,column = 1,sticky = tk.W)
    button = tk.Button(frame,text = "Submit",command= lambda:submit("Main"),bg = "red",fg = "yellow")
    button.grid(row = 5,columnspan = 2)

    status.config(text="")
    bookLabel.config(text="")
    totalLabel.config(text="")
    leftLabel.config(text="")

def Add_a_book():
    root.title("Add a book")
    isbnTextLabel.grid(row=1, column=0, sticky=tk.E)
    isbnLabel.grid(row=1, column=1, sticky=tk.W)
    bookTextLabel.grid(row=2, column=0, sticky=tk.E)
    bookEntry.grid(row=2, column=1, sticky=tk.W)
    totalTextLabel.grid(row=3, column=0, sticky=tk.E)
    totalEntry.grid(row=3, column=1, sticky=tk.W)
    leftTextLabel.grid_forget()
    leftLabel.grid_forget()
    button = tk.Button(frame, text="Submit", command=lambda: submit("Add_a_book"), bg="red", fg="yellow")
    button.grid(row=5, columnspan=2)

    status.config(text="")
    bookLabel.config(text="")
    totalLabel.config(text="")
    leftLabel.config(text="")
    pass

mainButton = tk.Button(frameMenu,text = "Main",fg = "red",bg = "yellow",command = Main)
mainButton.grid(row = 0,column = 0)
addButton = tk.Button(frameMenu,text = "Add_a_book",fg = "red",bg = "yellow",command = Add_a_book)
addButton.grid(row = 0,column = 1)
lendButton = tk.Button(frameMenu,text = "Lend",fg = "red",bg = "yellow",command = Lend)
lendButton.grid(row = 0,column = 2)
returnButton = tk.Button(frameMenu,text = "Return",fg = "red",bg = "yellow",command = Return)
returnButton.grid(row = 0,column = 3)

Main()

root.title("Library")
root.resizable(0,0)
root.mainloop()
