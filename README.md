# Moodroid

An app to log and view other peoples moods. 

## Initial Startup

1. ensure you setup a macro to automatically format on save: https://freakycoder.com/android-studio-tips-tricks-3-save-format-the-code-e8ea291fa84f
2. Setup the UML plugin we are going to be using if you want to visualize the structure: https://stackoverflow.com/questions/17123384/how-to-generate-class-diagram-uml-on-android-studio-intellij-idea/36823007#36823007
3. Optionally install the Javadoc plugin to document your code, otherwise use the Javadoc specifications: https://binfalse.de/2015/10/05/javadoc-cheats-sheet/ We will be autogenerating documentation for our code base on commits.

## Project Structure (From an architecture level)

We are attempting to create a highly decoupled application to allow simple testing, and simple development in the future. Because of doing this, we have a multi-tiered architecture in respect to the code base, using some techniques in the MVC pattern.

The most important tiers we have are:

1. Design/Fragment layer (AKA controllers, control the flow of the application and user input/output and communication to services)
2. Service Layer (Public facing business logic for use case implementation of the controller and manipulation of the models)
3. Model Layer (Public facing concrete object manipulation/serialization layer)
4. Repository Layer (protected object storage layer of models and integration layer with external services)

This allows the design implementation to only know about how to talk with services and what it expects back (because of interface contracts) so backend and frontend developers can work seemlessly without worrying about breaking on-anothers code.

## Terminology

- **mood event**: an entry of a mood by a specific user
- **model**: a concrete construct of an object in firestore
- **repository**: an implementation of C(reate)R(ead)U(pdate)D(elete) against some sort of persistent storage, in our case most likely Firestore
- **service**: a concrete implementation of business logic to be utilized by the controllers/views/fragments/activities in our application
