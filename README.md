#Readme File

This code was forked from Jim Gupta at https://code.google.com/p/linux-g13-driver/  

I've forked this code because Google Code is deprecated and I don't want it lost.  Since then, I've started playing with it a little bit (Mostly directory structure and semantic changes for use with git).  

I haven't changed the fundamental code because I'm by no means a coder, but we'll see what happens from here.  I'll eventually write a .service file for systemd and perhaps package it for the AUR.  The service file should work with Ubuntu 15.04 and above.

# Notes #
I've tried this on Arch Linux and it works so far.  I haven't tried it in Ubuntu but it should work there, too.


# Requirements #
libusb-1.0  
For Ubuntu, it should be installed already but if you don't have it, you can get it by typing:

    sudo apt-get install libusb-1.0-0
    
For Arch Linux you can install it by typing

    sudo pacman -S libusb

Java version 1.6 or higher  
For Ubuntu, it can be install by typing:

    sudo apt-get install default-jre
    
For Arch it can be installed by typing:

    sudo pacman -S jre8-openjdk

# Download #
Download zip file from https://github.com/Tetz95/linux-g13-driver/tree/master/distros  
Unzip into your favorite directory

# Build #
Open a console (command prompt)  
Go to the directory where you unzipped your download  
type ``make``


# Running Application #
Run the config tool first!
In a command prompt go to the directory where you unzipped your download and type:  

    java -jar Linux-G13-GUI.jar

This will bring up the UI and create the initial files needed for your driver.  
All config files are saved in ``$(HOME)/.g13``

Run the driver
In a command prompt go to the directory where you unzipped your download and type:  

    sudo -E ./G13-Linux-Driver

The ``-E`` is to run it using your environment variables so it doesn't look for the ``.g13`` directory in ``/root``  
If you want to run the command and then detach it so you can close the terminal:

    sudo -E ./G13-Linux-Driver &

If you are configuring the application while the driver is running, the driver will not pick up changes unless you select a different bindings set or you can restart the driver.

The top 4 buttons under the LCD screen select the bindings.

The joystick currently only supports key mappings.
