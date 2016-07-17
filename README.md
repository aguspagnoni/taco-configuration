# Taco Dressings

Developers
  - [Alderete, Facundo](https://github.com/facualderete)
  - [Elli, Federico](https://github.com/Federelli)
  - [Pagnoni, Agustín](https://github.com/aguspagnoni)
  - [Romarion, German](https://github.com/gromarion)

**Taco Dressings** is a Java powered Eclipse plgin for Taco-based test configuration edition.

### Installation

  - Clone this repository to your computer
  - Import the prject in Eclipse: File -> Import... -> General -> Existing Projects into Workspace
  - Open the `plugin.xml` file, located in the project's root folder:
    - Open the **Overview** tab
    - Follow the **Exporting** section's instructions:
      - Organize the plug-in using the **Organize Manifests Wizard**:
        - Once the Organize **Manifests Wizard** window is opened, click **Finish**.
      - Externalize the strings within the plug-in using the **Externalize Strings Wizard**
      - Specify what needts to be packaged in the deployable plug-in on the **Build Configuration** page. The following files must be included to the **Binary Build**:
        - **META-INF**\
            - `MANIFEST.MF`
        - **OSGI-INF**\
            - **I10n**\
                - `bundle.properties`
        - **lib**\
            - `jackson-annotations-2.3.0.jar`
            - `jackson-core-2.7.4.jar`
            - `jackson-databind-2.3.3.jar`
            - `jackson-dataformat-yaml-2.7.4.jar`
            - `snakeyaml-1.15.jar`
        - `plugin.xml`
        - `super_taco.png`
    - Export the plug-in in a format suitable for deployment using the **Export Wizard**:
        - Select the **Install into host. Repository:** option. Don't change the installation path. It should look something like: `WORKSPACE_PATH/.metadata/.plugins/org.eclipse.pde.core/install/`
    - Once the installation has finished, Eclipse will ask you to restart the IDE.
    
### Usage
**Taco Dressings** plug-in only works with `.java` files. Also, the file must contain a method that begins with `public void test_` to function.

In order to use the plug-in, **right-click** on a `.java` file, and select the **Taco Dressings** option. A window will be displayed, with the test configurations of the method. The ones that are not included in the method, will be filled with the default value.

Once you finished configuring all the methods, click on the **Save** button in the upper-right corner of the window. This will immediately override the current `.java` file with all the new configurations.
