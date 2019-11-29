# Vision
Programming languages usually require a lot of training to learn. They aren't intuitive to those who don't code. Expressing a complex algorithm can sometimes be very difficult in lower level languages.

Vision attempts to solve these problems with its simplicity and power. This programming language is built and interpreted through Java.
Using a simple syntax structure, someone can easily learn programming skills without straying from the familiar english language. 

# Overview
To use Vision, clone the repo into a workspace and have a .txt file handy. In a main method, create a Project, compile it, and start it. An example of this can be found in Main.java.

All the Vision code can go into the .txt file.

The entry of execution is the `when started` hat. To print something to standard out, use the `print []` statement. An `end` is necessary to close the hat.

    when started
    	print [Hello World]
    end

Simple, right? Here's how we can set "x" to "Hello World" and print it.

    when started
   		set [x] to [Hello World]
		print (x)
    end
	
You can also define custom commands and reporters.

    when started
		greet [Jake]
		print (roll dice)
	end
	
	define command greet [name]
		print [Hello #name]
	end
	
	define reporter roll dice
		return (random from (1) to (6))
	end
	
Commenting works by placing a '#' at the beginning of a line. 
    
	when started
		# When started, print Hello World
		print [Hello World]
	end
	
Here's an examples of some flow control.

	when started
		# (get age) will return the age of the user. 
		set [age] to (get age)
		if ((age) < (13))
			print [Child]
		else if ((age) < (18))
			print [Teenager]
		else
			print [Adult]
		end
	end
	
Lastly, an example of looping.

	when started
		# Will print the first 5 integers.
		for every [x] up to (5)
			print (x)
		end
	end
	
More examples can be found in the 'res' folder. 

# Future
There are many things that are planned for the future.
- [ ] Lists & Collections
- [ ] Objects
- [ ] GUI System
