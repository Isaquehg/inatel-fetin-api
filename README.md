# Inatel FETIN API

This is a Spring Boot API developed for managing the FETIN (Feira Tecnológica do Inatel) event at Inatel. It handles authentication via JWT, offers comprehensive documentation via Swagger, and supports full E2E testing. The deployment is managed through Google Cloud Run.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
  - [Authentication](#authentication)
  - [User Roles](#user-roles)
  - [CRUD Operations](#crud-operations)
  - [Documentation](#documentation)
- [License](#license)

## Project Overview

The FETIN Management API is designed to facilitate the organization and administration of the FETIN event at Inatel. It supports different user roles including Students, Advisors, and Administrators, allowing each role to perform specific tasks within the system.

## Features

### Authentication

- **JWT-based Authentication**: Ensures secure access to the API endpoints by issuing JSON Web Tokens.

### User Roles

- **Students**: Can participate in multiple projects and view their grades at each stage of the FETIN event.
- **Advisors**: Can supervise multiple projects and have the ability to view and manage student grades.
- **Administrators**: Can perform CRUD operations on advisors, teams, and students.

### CRUD Operations

- **Manage Students**: Create, Read, Update, and Delete student records.
- **Manage Advisors**: Create, Read, Update, and Delete advisor records.
- **Manage Teams**: Create, Read, Update, and Delete team records.

### Documentation

- **Swagger UI**: Comprehensive API documentation available via Swagger, allowing easy exploration and testing of API endpoints.

## License
MIT License

Copyright (c) 2024 Isaque Hollanda Gonçalves

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.