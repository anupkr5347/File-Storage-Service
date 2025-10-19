# File Storage System

A **Java Spring Boot** application for storing and retrieving files using **MinIO** for object storage and **PostgreSQL** for storing file metadata. This system allows uploading, downloading, and managing files efficiently in a cloud-native, scalable way.  

---

## Features

- Upload files to **MinIO object storage**
- Download files as **attachments** via REST API
- Store file metadata (filename, size, upload timestamp, etc.) in **PostgreSQL**
- Secure and scalable design
- Supports large files via streaming (`InputStream`)  
- Easily extendable to support authentication, versioning, or distributed storage  

---

## Tech Stack

| Layer                  | Technology                             |
|------------------------|----------------------------------------|
| Backend Framework      | Spring Boot                             |
| Object Storage         | MinIO (S3-compatible)                  |
| Database               | PostgreSQL                              |
| Build Tool             | Maven                                   |
| Language               | Java 21                                |
| REST API               | Spring Web                              |
| File Streaming         | InputStream / InputStreamResource       |
