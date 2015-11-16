# HKBU COMP4007 ATM-SS-Project Group 5

## Development rules

0. If you want to add or change any public methods or attributes to the classes defined in the class diagram, you must inform the team in WeChat first and make sure everyone agrees on these changes. Then you can push the code and everyone pulls the latest version. 

1. The code you are about to push to GitHub must be compatible with the latest version on GitHub. You should also update the development log clearly list out what you have done and inform the team in WeChat.


## Development log
###======================Nov 1, 2015 Freeman======================

0. Project website launched! :stuck_out_tongue_closed_eyes:
1. All classes defined in phase 1 have been created, yet the methods, attributes and detailed code of those classes are still empty. Some methods may be changed later. The class diagram has been updated accordingly. 
2. The sample codes provided by Joe are also included for now. Those code can only be > reference as mentioned in the lecture.

###======================Nov 11, 2015 Freeman======================

So far what we have done:

0. All the finctions of hardware emulators have finished. They are not fully tested though, maybe modified little in the future.
	To Check:
		0.1. Add functions for them accroding to the requirnment
		0.2. When the status of a emulator is set to be not working by Exceptioin emulator, all the functions (functionnal buttons) of the corresponding hw should be disabled
		0.3. The status of a hardware emulator can be changed by ATM-SS or Exceptioin emulator independently. The status lables on the Exception emulator should be synchronized.

1. Tony has finished the php part, need to be tested by handler.

###======================Nov 12, 2015 Freeman======================

0. Hardware Exception finished. The logic has changed a little.
	0.1 For each specific Exception has two constructors. 
	    The default constructor is for Unknown ones(not defined in the doc from Google drive) and Faltal errors.
	    Another constructor will only take the exception code as parameter, all the massage of a specific hardware is defined in the MSG string
	0.2 When the HardwareView creates a exception using the throwExceptin method need to check the status of the hardware emulator and see if it's Out of service status or say Fatal error that those statuses end with '99' will call the default constructor of cooresponding exception class, otherwise, it will call another constractor and pass the status as exception code to it.


###======================Nov 13, 2015 LIHUI======================

0. Almost complete DepositProcessController except for the timer
1. New methods needed in MainController:
    1.1 printReiceipt
    1.2 doBAMSCheckAccounts

###======================Nov 15, 2015 DJY======================

0.	Almost finished ChangePasswdController.java and WithDrawController.java,
	made some changes to Display emulator so that it now has two areas.
1.	Assumptions on the Display:
	it has two display areas, upper area and lower area,
	the upper area displays String[] line by line, 
	the current method "public boolean doDisplay(String[] contents)" is in charge of this area,
	can only replace the content with the new one.
	the lower area displays one line of String,
	need new methods in MainController to take care of this area,
	can append new content to the previous String, or clear all.
2.	Assumptions on the methods that return a boolean:
	such as "public boolean doDisplay(String[] contents)",
	when being called, if the hardware has error or cannot response in time,
	then it should return a false immediately.
	In other words, the MainController should be in charge of "timeout" issue.
3.	Needed methods:

	3.0	"public String[] doBAMSCheckAccounts()":
		normally returns an array of account number;
		when the relevant hardware/BAMS get timeout/error,
		returns a null or empty array immediately, so that the caller will know it failed.
		It is the same method required by LIHUI, although I prefer the name "doBAMSGetAccounts".
		Or should it take (String CardNumber) as its parameter???????
		
	3.1	"public String getPasswordFromUser()":
		normally returns a String of password format;
		keeps asking for user input if the format is wrong,
		and when the relevant hardware/user get timeout/error,
		returns an empty String immediately.
		
	3.2 "public boolean doBAMSUpdatePasswd(String cardNumber, String oldPassword, String newPassword)":
		change the current method "doBAMSUpdatePasswd(String accountNumber, String newPasswd)",
		because the password is card related and old password is needed.
		
	3.3 "public int getWithdrawAmountFromUser()":
		normally returns an positive integer that can be divided by 100;
		keeps asking for user input if the format is wrong, 
		and when the relevant hardware/user get timeout/error,
		returns 0 immediately.
		
	3.4 "public boolean doBAMSWithdraw(accountNumber, withdrawAmount)":
		returns true if BAMS approve the withdraw;
		returns false if BAMS disapprove it or get timeout/error;
		
	3.5 "public boolean collectInTime()":
		normally returns true if the cash is collected;
		returns false when cash is not collected in time.
		
4.	Current method: "public String doGetKeyInput()":
	should return a single character,
	and when the relevant hardware/user get timeout/error,
	should return empty String immediately.
5.	Concerns:
	Hardware like display can fail at any time but how can a processController know?
	Now the processController can only know this when calling the corresponding method,
	but the display can still fail after being called at any time.
6.	Shall we change the name "WithDrawController" to "WithdrawController" ?

###======================Nov 15, 2015 SXM======================

0.  Almost finished the EnquiryController and TransferController part in atmss.process package.
1.  The timer part has not been added.
2.  The GUI part of Exception Emulator seems not good on my computer. 
	Some contents are blocked and cannot be seen.
	I don't know whether it is my PC's problem or the program's problem.
	Pay some attention to that part.
3.  A question here: if a user type "CANCEL" button when he can, the whole process will end. 
	Should we handle that part in the process controller or we have another one to handle it?

###======================Nov 15, 2015 DJY======================

0.	Need this method or not?
	"public void doCDCheckInventory()":
	when a withdraw is complete the ATM needs to check its cash inventory.
	Is it automatically done? Currently, I am calling it after the cash get ejected.

###======================Nov 16, 2015 Freeman======================

0. Main functions of Advice printer, card reader and cash dispenser are provided in Main Controller please check and update if you are using them.
1. Note that all the exceptions will be handled in main controller that the process controller will only receive the result from the method you call. The result can be 
	boolean if this method does not returns anything, this boolean indicates the successfulness of this operation; 
	int if this is what the method provides, in this case if it returns -1 meaning the failure of this operation;
	object if this is what the method provides, in this case if it returns null meaning the failure of this operation.
3. For the parameter taken please refer to the code, some of the may be different from class diagram.

###======================Nov 16, 2015 Freeman======================

0. Timer in this version is OK to use, but becareful to use. Please read through the timer and Timer test before you use it. I have added line-by-line commons to explain the flow.

###======================Nov 16, 2015 Freeman======================

0.Finish keypad hardware, provide single input function; get passwd method is also provided but not finished, wait for adding display functions

	
## Notice
There is a "mistake" in BAMSHandler.java, line 134.
"toAcc=" should be "&toAcc=", whenever you try to use the transfer method, append a "&" behind the cred.

e.g

handler.transfer(cardNo, cred + "&", accNo, toAcc, amount)

## To be discussed

0. Discuss how to simulate hardware reset and shutdown, then implement.