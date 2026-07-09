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

### 🔒 Encapsulation
Internal inventory attributes like `quantity`, `expirationDate`, and `storageLocation` in the `Material` class are set to `private` or `protected`. They can only be modified through controlled methods like `addStock(int amount)` or `dispatchStock(int amount)`, preventing unauthorized or accidental manipulation of critical supply data.

### 🧬 Inheritance
A base class `Material` stores common properties (ID, name, quantity, threshold). Specific supply categories inherit from this base class to extend functionality without rewriting code, such as `PerishableMaterial` (adding `expirationDate`) and `MedicalMaterial` (adding `hazardLevel` or `requiresPrescription`).

### 🧩 Polymorphism
The base class `Material` declares an abstract method `checkUrgencyStatus()`. This is overridden differently across subclasses:
* `PerishableMaterial` overrides it to flag urgency based on days left until expiration.
* `RescueGear` overrides it based on whether stock levels fall below a critical safety threshold.

### 🔍 Abstraction
An `EmergencyResponse` interface or abstract class hides complex backend logistics. The frontend user interface simply invokes a method like `triggerSOP(String disasterType)` to fetch rules and supply kits without needing to manage the underlying database queries.

---

## System Architecture & Class Breakdown
* **`Material` (Base Class):** Represents a generic item in the inventory tracking base quantity and minimum safety thresholds.
* **`Donation` (Class):** Tracks donor details, date received, item type, and automatically routes data to update the `Material` inventory.
* **`SOPManager` (Class):** Handles the sub-menu for Standard Operating Procedures, mapping specific emergency types (e.g., Typhoon, Earthquake, Fire) to designated logistics checklists.

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
