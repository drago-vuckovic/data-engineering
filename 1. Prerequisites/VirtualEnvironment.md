# Python Virtual Environments

## Create Virtualenv and install dependencies

- Install Miniconda

    ```bash
    mkdir -p ~/miniconda3
    wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh -O ~/miniconda3/miniconda.sh
    bash ~/miniconda3/miniconda.sh -b -u -p ~/miniconda3
    rm -rf ~/miniconda3/miniconda.sh
    ```

- activate Miniconda

  ```bash
  conda init
  conda activate
  ```

- Check Miniconda Version:

    You can verify the Miniconda installation by checking its version using the following command:

    ```bash
    conda --version
    ```

    This command should display the version number of Miniconda that you installed. If it does, it indicates that Miniconda is correctly installed and available in your terminal.

- Check Python Version:

    You can also check the version of Python that Miniconda installed by running:

    ```bash
    python --version
    ```

    This should display the Python version installed by Miniconda.

- List Installed Environments:

    To see a list of environments installed by Miniconda, you can use the following command:

    ```bash
    conda env list
    ```

    This will display a list of Conda environments, including the base environment.

- Test a Conda Command:
    You can test Miniconda by creating a new Conda environment. For example:

    ```bash
    conda create -n test-env python=3.9
    ```

    This command will create a new Conda environment named "test-env" with Python 3.9. If it executes without errors, it's another indication that Miniconda is working correctly.

- You may delete the environment by running:

    ```bash
    conda env remove -n test-env
    ```