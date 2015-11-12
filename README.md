# HKBU COMP4007 ATM-SS-Project Group 5

## Development rules

0. If you want to add or change any public methods or attributes to the classes defined in the class diagram, you must inform the team in WeChat first and make sure everyone agrees on these changes. Then you can push the code and everyone pulls the latest version. 

1. The code you are about to push to GitHub must be compatible with the latest version on GitHub. You should also update the development log clearly list out what you have done and inform the team in WeChat.


## Development log
###======================Nov 1, 2015 Freeman======================

0. Project website launched! :stuck_out_tongue_closed_eyes:
1. All classes defined in phase 1 have been created, yet the methods, attributes and detailed code of those classes are still empty. Some methods may be changed later. The class diagram has been updated accordingly. 
2. The sample codes provided by Joe are also included for now. Those code can only be > reference as mentioned in the lecture.

==============


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

	
	
## Notice
There is a "mistake" in BAMSHandler.java, line 134.
"toAcc=" should be "&toAcc=", whenever you try to use the transfer method, append a "&" behind the cred.

e.g

handler.transfer(cardNo, cred + "&", accNo, toAcc, amount)