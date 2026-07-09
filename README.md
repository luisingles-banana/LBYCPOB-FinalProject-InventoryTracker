PROJECT TITLE:

DLSU Proactive Emergency Donations & Materials Inventory System
TEAM MEMBERS:

    Formales, Khain

    Ingles, Luis Kevin

    Mortel. Luck Orville 

PROBLEM STATEMENT & GOALS:

During crises, emergency response is often reactive, leading to bottlenecks, mismatched supply distribution, and confusion regarding protocols. This project aims to build a centralized database system that proactively tracks emergency donations and materials inventory, while providing quick access to Standard Operating Procedures (SOPs) to streamline disaster preparedness and relief operations.
TARGET USER:

    Disaster Response Administrators / Logistics Officers: To manage inventory levels, track expiration dates, and dispatch goods.

    Donors (Students/Faculty/External Partners): To view real-time needs and log incoming donations.

    Response Teams / Volunteers: To access emergency SOPs and coordinate distribution.

BRIEF DESCRIPTION:

A proactive emergency management platform featuring a centralized inventory database for disaster relief materials (medical supplies, food packs, rescue gear). The system tracks real-time stock levels against target thresholds, alerts administrators of shortages before disaster strikes, logs donor contributions, and hosts a dedicated sub-menu detailing specific SOPs for various crisis scenarios.
CORE OOP CONCEPTS:

    Encapsulation: * How: Internal inventory attributes like quantity, expirationDate, and storageLocation in the Material class will be set to private or protected. They can only be modified through controlled methods like addStock(int amount) or dispatchStock(int amount), preventing unauthorized or accidental manipulation of critical supply data.

    Inheritance: * How: A base class Material will store common properties (ID, name, quantity). Specific supply categories will inherit from it, such as PerishableMaterial (adding expirationDate) and MedicalMaterial (adding requiresPrescription or hazardLevel).

    Polymorphism: * How: The base class Material can have an abstract method checkUrgencyStatus(). A PerishableMaterial overrides this to mark urgency based on days left until expiration, while a RescueGear material overrides this based on whether stock falls below a critical safety threshold.

    Abstraction: * How: Creating an EmergencyResponse interface or abstract class that hides complex logistics computation. The frontend just calls triggerSOP(String disasterType) without needing to know the complex database queries running under the hood to fetch corresponding rules and supply kits.

INITIAL CLASS IDEAS:

    Material (Base Class): Represents an item in the inventory (e.g., ID, name, base quantity, minimum threshold).

    Donation (Class): Tracks donor details, date received, item type, and links directly to updating the Material inventory.

    SOPManager (Class): Handles the sub-menu for Standard Operating Procedures, mapping specific emergency types (e.g., Typhoon, Earthquake, Fire) to designated checklists and supply checklists.

USER STORIES:

    As a Logistics Officer, I want to view material stock levels relative to predefined safety thresholds so that I can proactively request donations before a shortage occurs.

    As a Donor, I want to see a real-time list of highly requested/low-stock emergency items so that my donation addresses immediate, actual needs.

    As a Response Team Volunteer, I want to access the SOP sub-menu during a crisis so that I can follow accurate deployment protocols and know exactly what supplies are allocated for the emergency.

CORE FEATURES:

    Dynamic Inventory Dashboard: Real-time tracking of incoming donations and outgoing dispatches with threshold alerts (e.g., Green = Safe, Yellow = Low, Red = Critical).

    Perishable & Expiration Tracking: Automatic sorting of materials ensuring items nearing expiration are deployed first (First-In, First-Out logistics approach).

    SOP & Emergency Protocol Sub-menu: A categorized library of crisis-specific guidelines, pairing each disaster type with a required checklist of materials.

    Donation Logging System: Allows public or internal logging of monetary or material contributions, automatically routing material types to their respective inventory slots.
