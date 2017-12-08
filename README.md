# prog5_jpate201_oconne16_dmehta22

README 


How to Compile:

	1.	Use Eclipse
	2.	Go to ServerMain.java
	3.	Click on little arrow next to Debug. Click on Debug Configurations
	4.	Locate Arguments tab, and under Program Arguments, type  a Port Number of your choice (e.g. 8192). Click Apply and Close
	5.	Then, run the ServerMain ( This starts the Server; see the console) 
	6.	Wait for the Server to run. Watch the console on Eclipse. 
	7.	Next, navigate to LoginFrame.java, to launch the GUI part of the Network Chat
	8.	Fill in Name and IP address (localhost) and the Port Number from Step 3. 
	9.	Repeat for as many clients as wanted…put to 10,000.
	10.	Chat.


Program details:

	•	The names used for each Client are not required to be unique because each Client receives a Unique Client ID automatically; which is guaranteed to not repeat (this allows two users to be uniquely identified, yet both still may use their favorite user name). 
	•	Although the directions suggest that we only use a 128 bit encryption; we opted to use a much more sophisticated encryption by using 2048 bits for both p and q. As a result, we decided to use the math library to generate our random large prime numbers, as an increased security measure (instead of having 20 vulnerable prime values in a resource file).
	•	For user prime input number, we will only be excepting values are secure (i.e p and q both must be 2048 bits each) 


