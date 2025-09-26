# Deployment Guide

This guide covers various deployment options for the Missing Person & Lost Item Tracker application.

## Table of Contents
1. [Local Development](#local-development)
2. [Docker Deployment](#docker-deployment)
3. [AWS Deployment](#aws-deployment)
4. [Azure Deployment](#azure-deployment)
5. [Environment Variables](#environment-variables)
6. [Security Considerations](#security-considerations)

## Local Development

### Prerequisites
- Java 17+
- Node.js 18+
- Python 3.9+
- MongoDB 7.0+

### Step-by-step Setup

1. **Clone and Setup Repository**
   ```bash
   git clone https://github.com/Atharv9406/Lost-Found-Person.git
   cd Lost-Found-Person
   ```

2. **Start MongoDB**
   ```bash
   # Using Docker
   docker run -d --name mongodb -p 27017:27017 mongo:7.0
   
   # Or install locally and start
   mongod --dbpath /path/to/data
   ```

3. **Backend Setup**
   ```bash
   cd backend
   
   # Build and run
   mvn clean install
   mvn spring-boot:run
   
   # Or using Maven wrapper
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

4. **Frontend Setup**
   ```bash
   cd frontend
   
   # Install dependencies
   npm install
   
   # Start development server
   npm start
   ```

5. **AI Service Setup**
   ```bash
   cd deployment/ai-service
   
   # Create virtual environment
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   
   # Install dependencies
   pip install -r requirements.txt
   
   # Start service
   python app.py
   ```

## Docker Deployment

### Using Docker Compose (Recommended)

1. **Prepare Environment**
   ```bash
   cd deployment
   
   # Copy and edit environment files
   cp .env.example .env
   # Edit .env with your configuration
   ```

2. **Start All Services**
   ```bash
   docker-compose up -d
   ```

3. **Check Service Status**
   ```bash
   docker-compose ps
   docker-compose logs -f [service-name]
   ```

4. **Stop Services**
   ```bash
   docker-compose down
   ```

### Manual Docker Setup

1. **Build Images**
   ```bash
   # Backend
   cd backend
   docker build -t missing-person-backend .
   
   # Frontend
   cd ../frontend
   docker build -t missing-person-frontend .
   
   # AI Service
   cd ../deployment/ai-service
   docker build -t missing-person-ai .
   ```

2. **Run Containers**
   ```bash
   # MongoDB
   docker run -d --name mongodb -p 27017:27017 mongo:7.0
   
   # Backend
   docker run -d --name backend --link mongodb:mongodb -p 8080:8080 \
     -e MONGODB_URI=mongodb://mongodb:27017/missing_person_tracker \
     missing-person-backend
   
   # AI Service
   docker run -d --name ai-service --link mongodb:mongodb -p 5000:5000 \
     missing-person-ai
   
   # Frontend
   docker run -d --name frontend --link backend:backend -p 80:80 \
     missing-person-frontend
   ```

## AWS Deployment

### Using AWS ECS with Fargate

1. **Prerequisites**
   - AWS CLI configured
   - Docker images pushed to ECR
   - VPC and subnets configured

2. **Create ECR Repositories**
   ```bash
   aws ecr create-repository --repository-name missing-person-backend
   aws ecr create-repository --repository-name missing-person-frontend
   aws ecr create-repository --repository-name missing-person-ai
   ```

3. **Build and Push Images**
   ```bash
   # Get login token
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 123456789012.dkr.ecr.us-east-1.amazonaws.com
   
   # Build and push backend
   cd backend
   docker build -t missing-person-backend .
   docker tag missing-person-backend:latest 123456789012.dkr.ecr.us-east-1.amazonaws.com/missing-person-backend:latest
   docker push 123456789012.dkr.ecr.us-east-1.amazonaws.com/missing-person-backend:latest
   
   # Repeat for frontend and AI service
   ```

4. **Create ECS Cluster**
   ```bash
   aws ecs create-cluster --cluster-name missing-person-cluster
   ```

5. **Deploy Services**
   ```bash
   # Use the provided ECS task definitions
   aws ecs register-task-definition --cli-input-json file://ecs-backend-task.json
   aws ecs register-task-definition --cli-input-json file://ecs-frontend-task.json
   
   # Create services
   aws ecs create-service --cluster missing-person-cluster --service-name backend --task-definition backend:1 --desired-count 2
   aws ecs create-service --cluster missing-person-cluster --service-name frontend --task-definition frontend:1 --desired-count 2
   ```

### Using AWS Elastic Beanstalk

1. **Create Application**
   ```bash
   eb init missing-person-tracker --region us-east-1 --platform "Docker"
   ```

2. **Deploy**
   ```bash
   eb create production --instance-type t3.medium
   eb deploy
   ```

## Azure Deployment

### Using Azure Container Instances

1. **Create Resource Group**
   ```bash
   az group create --name missing-person-rg --location eastus
   ```

2. **Create Container Registry**
   ```bash
   az acr create --resource-group missing-person-rg --name missingpersonacr --sku Basic
   ```

3. **Build and Push Images**
   ```bash
   # Login to ACR
   az acr login --name missingpersonacr
   
   # Build and push
   docker build -t missingpersonacr.azurecr.io/backend:latest ./backend
   docker push missingpersonacr.azurecr.io/backend:latest
   ```

4. **Deploy Container Group**
   ```bash
   az container create \
     --resource-group missing-person-rg \
     --name missing-person-app \
     --image missingpersonacr.azurecr.io/backend:latest \
     --cpu 1 --memory 1 \
     --registry-login-server missingpersonacr.azurecr.io \
     --registry-username <username> \
     --registry-password <password> \
     --dns-name-label missing-person-app \
     --ports 8080
   ```

### Using Azure App Service

1. **Create App Service Plan**
   ```bash
   az appservice plan create --name missing-person-plan --resource-group missing-person-rg --sku B1 --is-linux
   ```

2. **Create Web App**
   ```bash
   az webapp create --resource-group missing-person-rg --plan missing-person-plan --name missing-person-backend --deployment-container-image-name missingpersonacr.azurecr.io/backend:latest
   ```

## Environment Variables

### Backend (.env or environment variables)
```bash
# Database
MONGODB_URI=mongodb://localhost:27017/missing_person_tracker

# Security
JWT_SECRET=your-super-secret-jwt-key-here
JWT_EXPIRATION=86400000

# Firebase
FIREBASE_CONFIG_PATH=/path/to/firebase-service-account.json

# AI Services
FACE_RECOGNITION_ENDPOINT=http://localhost:5000/api/face-recognition
OBJECT_DETECTION_ENDPOINT=http://localhost:5000/api/object-detection

# CORS
CORS_ORIGINS=http://localhost:3000,https://yourdomain.com

# Notification
NOTIFICATION_RADIUS=10
```

### Frontend (.env)
```bash
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_GOOGLE_MAPS_API_KEY=your-google-maps-api-key
GENERATE_SOURCEMAP=false
```

### AI Service (.env)
```bash
MONGO_URI=mongodb://localhost:27017/missing_person_tracker
FLASK_ENV=production
PORT=5000
```

## Security Considerations

### SSL/TLS Configuration

1. **Obtain SSL Certificates**
   ```bash
   # Using Let's Encrypt
   certbot certonly --webroot -w /var/www/html -d yourdomain.com
   ```

2. **Configure Nginx**
   ```nginx
   server {
       listen 443 ssl;
       server_name yourdomain.com;
       
       ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
       ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
       
       # Additional SSL configuration
       ssl_protocols TLSv1.2 TLSv1.3;
       ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512;
   }
   ```

### Database Security

1. **MongoDB Authentication**
   ```javascript
   // Enable authentication in MongoDB
   use admin
   db.createUser({
     user: "admin",
     pwd: "secure-password",
     roles: ["userAdminAnyDatabase"]
   })
   ```

2. **Network Security**
   - Use VPC/Virtual Networks
   - Configure security groups/network security groups
   - Restrict database access to application servers only

### Application Security

1. **Environment Variables**
   - Never commit secrets to version control
   - Use secure secret management (AWS Secrets Manager, Azure Key Vault)
   - Rotate secrets regularly

2. **CORS Configuration**
   ```java
   @CrossOrigin(origins = {"https://yourdomain.com"})
   ```

3. **Rate Limiting**
   - Implement rate limiting on API endpoints
   - Use services like AWS WAF or Cloudflare

## Monitoring and Logging

### Application Monitoring

1. **Health Checks**
   ```bash
   # Backend health check
   curl http://localhost:8080/actuator/health
   
   # AI service health check
   curl http://localhost:5000/health
   ```

2. **Logging Configuration**
   ```yaml
   # application.yml
   logging:
     level:
       com.lostfound.tracker: DEBUG
     pattern:
       file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
     file:
       name: /logs/application.log
   ```

### Container Monitoring

1. **Docker Stats**
   ```bash
   docker stats
   docker logs -f container-name
   ```

2. **Cloud Monitoring**
   - AWS CloudWatch
   - Azure Monitor
   - Prometheus + Grafana

## Backup and Recovery

### Database Backup

1. **MongoDB Backup**
   ```bash
   # Create backup
   mongodump --uri="mongodb://localhost:27017/missing_person_tracker" --out=/backup/

   # Restore backup
   mongorestore --uri="mongodb://localhost:27017/missing_person_tracker" /backup/missing_person_tracker/
   ```

2. **Automated Backups**
   ```bash
   # Cron job for daily backups
   0 2 * * * /usr/local/bin/mongodump --uri="$MONGODB_URI" --out=/backup/$(date +\%Y\%m\%d)/
   ```

## Troubleshooting

### Common Issues

1. **Port Conflicts**
   ```bash
   # Check what's using a port
   lsof -i :8080
   netstat -tulpn | grep :8080
   ```

2. **Memory Issues**
   ```bash
   # Check memory usage
   free -h
   docker stats --no-stream
   ```

3. **Database Connection Issues**
   ```bash
   # Test MongoDB connection
   mongo --eval "db.adminCommand('ismaster')"
   ```

### Log Analysis

```bash
# Backend logs
tail -f /logs/application.log

# Container logs
docker logs -f backend-container

# System logs
journalctl -u docker -f
```