# Proactive Emergency Donations & Materials Inventory System

## Project Description
During crises, emergency response is often reactive, leading to bottlenecks, mismatched supply distribution, and confusion regarding protocols. This project aims to build a centralized database system that proactively tracks emergency donations and materials inventory, while providing quick access to Standard Operating Procedures (SOPs) to streamline disaster preparedness and relief operations at De La Salle University.

The system features a centralized inventory database for disaster relief materials (medical supplies, food packs, rescue gear) that tracks real-time stock levels against target thresholds. It alerts administrators of shortages before disaster strikes, logs donor contributions, and hosts a dedicated sub-menu detailing specific SOPs for various crisis scenarios.

---

## Team Members
* **Formales, Khain** 
* **Ingles, Luis Kevin** 
* **Mortel, Luck Orville** 

---

## Target Users
* **Disaster Response Administrators / Logistics Officers:** To manage inventory levels, track expiration dates, and dispatch goods.
* **Donors (Students, Faculty, & External Partners):** To view real-time needs and log incoming donations.
* **Response Teams / Volunteers:** To access emergency SOPs and coordinate rapid distribution.

---

## Core OOP Concepts Applied

### Encapsulation

### Inheritance

### Polymorphism

### Abstraction

---

## System Architecture & Class Breakdown

---

## User Stories
* **As a Logistics Officer**, I want to **view material stock levels relative to safety thresholds** so that **I can proactively request donations before a shortage occurs.**
* **As a Donor**, I want to **see a real-time list of highly requested/low-stock emergency items** so that **my donation addresses immediate, actual needs.**
* **As a Response Team Volunteer**, I want to **access the SOP sub-menu during a crisis** so that **I can follow accurate deployment protocols and verify allocated supplies.**

---

## Core Features
* **Dynamic Inventory Dashboard:** Real-time tracking of incoming donations and outgoing dispatches with status alerts (`Green = Safe`, `Yellow = Low`, `Red = Critical`).
* **Perishable & Expiration Tracking:** Automatic sorting of materials ensuring items nearing expiration are flagged for deployment first (First-In, First-Out).
* **SOP & Emergency Protocol Sub-menu:** A categorized library of crisis-specific guidelines pairing disaster scenarios with their required supply checklists.
* **Donation Logging System:** Processes and tracks public or internal contributions, assigning items to their correct classification automatically.
