
Java Chat Application (Swing + Socket Programming)

A real-time multi-client chat application built using Java Socket Programming and Swing GUI.
The project demonstrates client–server communication, multithreading, and a modern desktop UI inspired by WhatsApp-style design.

🚀 Features

🔗 One server, multiple clients

💬 Real-time message broadcasting

🖥️ Modern Swing-based GUI

⏱️ Timestamped messages

🧵 Multithreaded client handling

🔒 Thread-safe UI updates

🚪 Exit notification when a user leaves

🌐 Supports LAN & Internet deployment

🛠️ Tech Stack

Java

Socket Programming (TCP/IP)

Java Swing (GUI)

Multithreading

AWT & Event Handling

🏗️ Project Structure
Chat-Application/
│
├── server/
│   └── ChatServer.java
│
├── client/
│   ├── ChatClient.java
│   └── ChatClientGUI.java
│
├── README.md
└── ChatClient.jar (optional)

⚙️ How It Works

The ChatServer listens on a specific port.

Each ChatClient connects using a socket.

Server creates a separate thread for every client.

Messages sent by one client are broadcast to all connected clients.

GUI updates are handled safely using SwingUtilities.invokeLater().

