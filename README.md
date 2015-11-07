# HKBU COMP4007 ATM-SS-Project

## Development log
======================Nov 1st, 2015 Freeman======================

0. Project website launched! :stuck_out_tongue_closed_eyes:
1. All classes defined in phase 1 have been created, yet the methods, attributes and detailed code of those classes are still empty. Some methods may be changed later. The class diagram has been updated accordingly. 
2. The sample codes provided by Joe are also included for now. Those code can only be > reference as mentioned in the lecture.

==============


### Notice
There is a "mistake" in BAMSHandler.java, line 134.
"toAcc=" should be "&toAcc=", whenever you try to use the transfer method, append a "&" behind the cred.
e.g
handler.transfer(cardNo, cred + "&", accNo, toAcc, amount)