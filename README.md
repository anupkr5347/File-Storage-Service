# 📁 File Storage System

A **Java Spring Boot** application for storing and retrieving files using multiple cloud and on-premise object storage options — **AWS S3**, **Google Cloud Storage (GCP Bucket)**, and **MinIO** — along with **PostgreSQL** for storing file metadata.  
This system provides REST APIs to upload, download, and manage files efficiently in a cloud-native, scalable, and vendor-agnostic way.

---

## 🚀 Features

- Upload and download files from:
  - **AWS S3**
  - **Google Cloud Storage (GCP Bucket)**
  - **MinIO (S3-compatible local storage)**
- Store file metadata (file name, size, timestamp, bucket source, etc.) in **PostgreSQL**
- Stream large files using `InputStream` (memory-efficient)
- Download files as attachments via REST API
- Easily switch between storage providers via environment configuration
- Cloud-agnostic and microservice-ready design
- Extendable for authentication, versioning, or distributed deployment

---

## 🧱 Tech Stack

| Layer                  | Technology / Service                        |
|------------------------|---------------------------------------------|
| **Backend Framework**  | Spring Boot                                 |
| **Cloud Storage**      | AWS S3, Google Cloud Storage (GCS), MinIO   |
| **Database**           | PostgreSQL                                  |
| **Build Tool**         | Maven                                       |
| **Language**           | Java 21                                     |
| **REST API**           | Spring Web                                  |
| **File Streaming**     | InputStream / InputStreamResource           |
| **Cloud SDKs**         | AWS SDK v2, Google Cloud Storage SDK        |

---

## ⚙️ Configuration

The application dynamically selects which storage to use based on input by user

