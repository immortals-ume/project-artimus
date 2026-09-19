# Book My Show - Low Level Design

This document outlines the low-level design of the Book My Show application, which is a platform for booking movie
tickets online. The design focuses on the core components, their interactions, and the data flow within the system.

Ticket booking system

theater
hall - show
List<Seat>
Seat (STATUS AVAILABLE, BOOKED, RESERVED)  -> concurrency problem if 2 users came to book seat what happens 