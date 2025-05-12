# Phonebook Service

## Overview

This is a reactive Phonebook API service built with Spring Boot, R2DBC, and PostgreSQL.\
The service provides a robust, scalable solution for managing contact information with advanced search capabilities.


## Features

- 🔒 Reactive REST API with R2DBC
- 📞 Contact management (Create, Read, Update, Delete)
- 🔍 Advanced full-text search capabilities
- 📱 Phone number normalization and validation
- 🌐 Multilingual support
- 📊 Performance-optimized database interactions


## Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Reactive Programming**: Project Reactor
- **Database**: PostgreSQL
- **Database Access**: R2DBC
- **Deployment**: Docker-compose
- **Documentation**: OpenAPI/Swagger


### Running the Application

#### Using Docker
```bash
# Build the application image, 
# and deploy everything (both service and DB)
docker compose up -d --build
```

## API Documentation

The API is documented using OpenAPI (Swagger). Check `openapi.yaml`.\
You can use `openapi.yaml` with:
- Postman: create API collection by importing this file. Then, easily send requests to the service while it's running from Postman.
- Swagger editor: copy the file content into Swagger editor for API visualization.

## Key Endpoints

- `POST /contacts`: Create a new contact
- `GET /contacts`: List contacts
- `GET /contacts/{id}`: Get contact by ID
- `GET /contacts/search`: Search contacts with free-text
- `PUT /contacts/{id}`: Update a contact
- `DELETE /contacts/{id}`: Delete a contact

## Search Capabilities

The API supports advanced full-text search with:
- Prefix matching
- Relevance ranking
- Fuzzy matching

Example search queries:
- Search by name: `GET api/contacts/search?query=John`
- Search by phone: `GET api/contacts/search?query=+97254`
- Search by address: `GET api/contacts/search?query=tel-aviv`

## Performance Optimizations

- Reactive programming model
- Full-text search with PostgreSQL GIN indexes
- Phone number normalization
- Efficient database querying with trigram index (pg_trgm)

## Testing

Run tests using:
```bash
gradle test
```

## Deployment Considerations

- Use environment variables for sensitive configuration
- Consider Kubernetes/containerization for scaling


