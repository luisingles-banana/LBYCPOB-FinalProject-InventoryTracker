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
Private fields across all model classes (Item, Donation, Donor, EmergencyContact, SOP) are protected from direct mutation; state changes are only permitted through validated methods like addStock() and dispatch(), preventing invalid stock levels.
### Inheritance
FoodPack, MedicalSupply, and RescueGear all extend the abstract Item class, inheriting shared fields (quantity, thresholds, expirationDate) and behavior (addStock(), dispatch()) without duplicating code across the three material types.
### Polymorphism
Each subclass overrides getCategory() to return its own fixed label ("Food Pack", "Medical Supply", "Rescue Gear"), so code that handles a list of Item objects can call the same method and get type-appropriate behavior without checking the concrete class.
### Abstraction
Item is declared abstract specifically to hide implementation differences between material types behind one shared interface; callers work with Item's public methods without knowing or caring which concrete subtype they're holding, and internal serialization details (escape(), toCsvRow()) stay hidden from the rest of the system.
---

## System Architecture & Class Breakdown
### Inventory Layer
Item (abstract) — base class defining shared stock fields and behavior (addStock, dispatch, thresholds).
FoodPack, MedicalSupply, RescueGear — concrete item types, each fixing its own category.
Database / DatabaseManager — handle per-inventory persistence (CSV read/write) and switching between named inventories.

### Donations Layer
Donor — represents a contributor (name, contact).
Donation — an immutable record linking a Donor to an item, quantity, and timestamp.

### Emergency Contacts
EmergencyContact — mutable contact entry (name, number, category, priority) for response teams.
EmergencyContactRepository — stores and retrieves contact entries.

### SOP Library
SOP — immutable record pairing a crisis scenario with its protocol steps and required supplies.
SOPRepository — stores and retrieves SOP entries by scenario.

### Menu/UI Layer
BaseMenu (abstract) — Template Method base for all CLI sub-menus, defining a shared display/input loop that concrete menus (inventory, donations, SOPs, contacts) fill in.
---

## User Stories

---

## Core Features
* **Dynamic Inventory Dashboard:** Real-time tracking of incoming donations and outgoing dispatches with status alerts (`Green = Safe`, `Yellow = Low`, `Red = Critical`).
* **Perishable & Expiration Tracking:** Automatic sorting of materials ensuring items nearing expiration are flagged for deployment first (First-In, First-Out).
* **SOP & Emergency Protocol Sub-menu:** A categorized library of crisis-specific guidelines pairing disaster scenarios with their required supply checklists.
* **Donation Logging System:** Processes and tracks public or internal contributions, assigning items to their correct classification automatically.
