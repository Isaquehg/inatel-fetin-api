# Inatel FETIN API

This is a Spring Boot API developed for managing the FETIN (Feira Tecnológica do Inatel) event at Inatel. It handles authentication via JWT, offers comprehensive documentation via Swagger, and supports full E2E testing. The deployment is managed through Google Cloud Run.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
  - [Authentication](#authentication)
  - [User Roles](#user-roles)
  - [CRUD Operations](#crud-operations)
  - [Documentation](#documentation)

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
