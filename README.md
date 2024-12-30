
# Docker Spring Boot MongoDB Project


This project demonstrates the integration of a Spring Boot application with MongoDB, utilizing (TODO Docker for containerization). It's designed for developers looking to implement CRUD operations on a MongoDB database through a Spring Boot application.

## Description

The Docker Spring Boot MongoDB project provides a RESTful API for managing student entities. It includes operations for creating, reading, updating, and deleting student records in a MongoDB database. The project showcases the use of Spring Data MongoDB for repository management, Spring Web for RESTful services, and (TODO Docker) for easy deployment and environment consistency.

## Getting Started

### Dependencies

- Java JDK 11 or higher
- Maven
- Docker
- MongoDB


### Installing

1. Clone the repository to your local machine:

```bash
git clone https://github.com/Ahmet-Acik/DockerSpringBootMongoDB.git
cd DockerSpringBootMongoDB
```
 ### Installing MongoDB if not installed, and how to start it to work with the project can follow these steps:

1. **Check if MongoDB is installed**: Provide a command or instructions to check if MongoDB is already installed on the user's system.

2. **Download MongoDB**: Include a link to the MongoDB official download page and instructions for selecting the correct version based on the user's operating system.

3. **Install MongoDB**: Offer step-by-step instructions for installing MongoDB. These instructions will vary based on the operating system.

4. **Start MongoDB**: Provide commands to start the MongoDB server on the user's machine.

5. **Verify MongoDB is running**: Suggest a command to check that MongoDB is running correctly.

6. **Connect the project to MongoDB**: Explain any steps needed to configure the project to connect to the local MongoDB instance, such as updating application properties with the correct database URI.


```markdown
## Installing and Running MongoDB Locally

If you do not have MongoDB installed on your system, follow these steps to download, install, and start MongoDB.

### Check if MongoDB is Installed

Open your terminal and run:

```bash
mongo --version
```

If MongoDB is installed, you should see the version information. If not, follow the steps below to install it.

### Download MongoDB

1. Visit the MongoDB official download page: [MongoDB Download Center](https://www.mongodb.com/try/download/community)
2. Select the version suitable for your operating system.
3. Download the installer or package.

### Install MongoDB

#### For Windows

- Run the MongoDB installer you downloaded.
- Follow the installation wizard steps.
- Add MongoDB's `bin` folder to your system's PATH environment variable.

#### For macOS

- You can use Homebrew to install MongoDB:

```bash
brew tap mongodb/brew
brew install mongodb-community
```

#### For Linux

- The installation steps can vary depending on your Linux distribution. Please follow the instructions on the MongoDB website for your specific distribution.

### Start MongoDB

#### For Windows

- Open Command Prompt as Administrator and run:

```bash
net start MongoDB
```

#### For macOS and Linux

- Run the following command in your terminal:

```bash
brew services start mongodb-community
```

### Verify MongoDB is Running

- You can verify that MongoDB is running by connecting to the database server using the MongoDB shell:

```bash
mongo
```

### Connect the Project to MongoDB

- Ensure your project's `application.properties` or `application.yml` file is configured to connect to your local MongoDB instance. Typically, the default URI is:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/yourDatabaseName
```

Replace `yourDatabaseName` with the name of your database.

After MongoDB is set up and running, you can proceed with the project installation and execution steps as described in the previous sections.
```

This guide assumes a general approach to installing MongoDB and may need adjustments based on specific user environments or newer versions of MongoDB.
2. Build the project using Maven:

```bash
mvn clean install
```

3. For (Docker TODO Run the Docker container):

```bash
docker-compose up --build
```

### Executing program

After successfully building and running the project (TODO Docker container), the Spring Boot application will be accessible at `http://localhost:8080`.

## API Reference

### CRUD Operations

#### Create a New Student

- **Endpoint:** `POST /students`
- **Content-Type:** `application/json`
- **Request Body:**

```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "age": 21
}
```

- **Response:**

```json
{
  "message": "A new student is successfully created with ID: <student_id>"
}
```

#### Retrieve All Students

- **Endpoint:** `GET /students/all`
- **Response:**

```json
[
  {
    "id": "<student_id>",
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "age": 21
  }
]
```

#### Retrieve a Student by ID

- **Endpoint:** `GET /students/{id}`
- **Response:**

```json
{
  "id": "<student_id>",
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "age": 21
}
```

#### Update a Student

- **Endpoint:** `PUT /students/{id}`
- **Content-Type:** `application/json`
- **Request Body:**

```json
{
  "name": "Jane Doe Updated",
  "email": "jane.updated@example.com",
  "age": 22
}
```

- **Response:**

```json
{
  "message": "Student updated successfully with ID: <student_id>",
  "student": {
    "id": "<student_id>",
    "name": "Jane Doe Updated",
    "email": "jane.updated@example.com",
    "age": 22
  }
}
```

#### Partially Update a Student

- **Endpoint:** `PATCH /students/{id}`
- **Content-Type:** `application/json`
- **Request Body:**

```json
{
  "email": "jane.partially.updated@example.com"
}
```

- **Response:**

```json
{
  "message": "Student partially updated successfully with ID: <student_id>",
  "student": {
    "id": "<student_id>",
    "email": "jane.partially.updated@example.com"
  }
}
```

#### Delete a Student

- **Endpoint:** `DELETE /students/{id}`
- **Response:**

```json
{
  "message": "Student with ID <student_id> deleted successfully."
}
```

## Tests

To run the tests, execute the following command:

```bash
mvn test
```

## Deployment

TODO The project includes a `Dockerfile` and `docker-compose.yml` for easy deployment. Use Docker Compose to build and run the application container alongside a MongoDB container.

## Contributing

Contributions are welcome! For major changes, please open an issue first to discuss what you would like to change.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/Ahmet-Acik/DockerSpringBootMongoDB/tags).

## Authors

- Ahmet Acik - *Initial work* - [Ahmet-Acik](https://github.com/Ahmet-Acik)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- Thanks to the Spring Boot and MongoDB communities for their excellent documentation and support.
```
