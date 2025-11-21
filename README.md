# Missing Person & Lost Item Tracker

The Missing Person & Lost Item Tracker is an AI-powered web and mobile platform that helps quickly report and locate missing individuals or lost belongings. It uses facial recognition, object detection, and real-time geo-alerts to match reports and notify nearby users and authorities for faster recovery.

## 🚀 Features

### Core Features
- **User Account Management**: Secure registration, login, and profile management
- **Incident Reporting**: Report missing persons or lost items with detailed information
- **AI-Powered Matching**: Facial recognition for missing persons and object detection for lost items
- **Real-time Notifications**: Push notifications via Firebase Cloud Messaging
- **Geo-location Tracking**: Google Maps integration for location-based searches
- **Admin Dashboard**: Administrative tools for managing reports and users
- **Mobile Responsive**: Works seamlessly on web and mobile devices

### AI Capabilities
- **Facial Recognition**: Advanced face encoding and matching algorithms
- **Object Detection**: Identify and match lost items in images
- **Similarity Scoring**: AI-powered confidence ratings for matches
- **Real-time Processing**: Fast image analysis and matching

### Technical Features
- **REST API**: Comprehensive backend API built with Spring Boot
- **Real-time Updates**: WebSocket support for live notifications
- **Secure Authentication**: JWT-based authentication system
- **Cloud-Ready**: Containerized deployment for AWS/Azure
- **Scalable Architecture**: Microservices-based design

## 🛠 Technology Stack

### Backend
- **Java Spring Boot 3.2.0**: Main backend framework
- **MongoDB**: NoSQL database for flexible data storage
- **Spring Security**: Authentication and authorization
- **JWT**: Token-based authentication
- **Firebase Admin SDK**: Push notifications
- **Maven**: Dependency management

### Frontend
- **React 18**: Modern frontend framework
- **TypeScript**: Type-safe JavaScript
- **Material-UI**: Component library for consistent UI
- **React Router**: Client-side routing
- **Axios**: HTTP client for API calls
- **Leaflet**: Interactive maps

### AI Services
- **Python Flask**: AI service backend
- **OpenCV**: Computer vision library
- **Face Recognition**: Facial recognition algorithms
- **TensorFlow**: Machine learning framework
- **NumPy**: Numerical computing

### DevOps & Deployment
- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration
- **Nginx**: Reverse proxy and static file serving
- **AWS/Azure**: Cloud deployment options

## 📋 Prerequisites

- **Java 17** or higher
- **Node.js 18** or higher
- **Python 3.9** or higher
- **MongoDB 7.0** or higher
- **Docker** and **Docker Compose** (for containerized deployment)

## 🚀 Quick Start

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Atharv9406/Lost-Found-Person.git
   cd Lost-Found-Person
   ```

2. **Start MongoDB**
   ```bash
   docker run -d --name mongodb -p 27017:27017 mongo:7.0
   ```

3. **Backend Setup**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```
   The backend will be available at `http://localhost:8080`

4. **Frontend Setup**
   ```bash
   cd frontend
   npm install
   npm start
   ```
   The frontend will be available at `http://localhost:3000`

5. **AI Service Setup**
   ```bash
   cd deployment/ai-service
   pip install -r requirements.txt
   python app.py
   ```
   The AI service will be available at `http://localhost:5000`

### Docker Deployment

1. **Using Docker Compose**
   ```bash
   cd deployment
   docker-compose up -d
   ```

This will start all services:
- Frontend: `http://localhost:80`
- Backend API: `http://localhost:8080`
- AI Service: `http://localhost:5000`
- MongoDB: `localhost:27017`

## 📁 Project Structure

```
Lost-Found-Person/
├── backend/                 # Spring Boot backend
│   ├── src/main/java/      # Java source code
│   ├── src/main/resources/ # Configuration files
│   ├── pom.xml            # Maven dependencies
│   └── Dockerfile         # Backend container
├── frontend/               # React frontend
│   ├── src/               # React source code
│   ├── public/            # Static assets
│   ├── package.json       # NPM dependencies
│   └── Dockerfile         # Frontend container
├── deployment/            # Deployment configurations
│   ├── ai-service/       # Python AI service
│   ├── docker-compose.yml # Local deployment
│   └── aws-deploy.yml    # AWS ECS deployment
└── docs/                 # Documentation
```

## 🔧 Configuration

### Backend Configuration
Edit `backend/src/main/resources/application.properties`:

```properties
# Database
spring.data.mongodb.uri=mongodb://localhost:27017/missing_person_tracker

# JWT Security
jwt.secret=your-secret-key
jwt.expiration=86400000

# Firebase (for push notifications)
firebase.config.path=path/to/firebase-service-account.json

# AI Services
ai.face.recognition.endpoint=http://localhost:5000/api/face-recognition
ai.object.detection.endpoint=http://localhost:5000/api/object-detection
```

### Frontend Configuration
Create `frontend/.env`:

```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_GOOGLE_MAPS_API_KEY=your-google-maps-api-key
```

## 📱 API Documentation

### Authentication Endpoints
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration

### Report Endpoints
- `GET /api/reports` - Get all public reports
- `POST /api/reports` - Create new report
- `GET /api/reports/{id}` - Get specific report
- `GET /api/reports/my-reports` - Get user's reports
- `GET /api/reports/nearby` - Get nearby reports
- `PUT /api/reports/{id}/status` - Update report status

### AI Service Endpoints
- `POST /api/face-recognition` - Encode faces in images
- `POST /api/compare-faces` - Compare face encodings
- `POST /api/object-detection` - Detect objects in images

## 🌐 Cloud Deployment

### AWS Deployment

1. **Setup AWS ECS Cluster**
   ```bash
   aws ecs create-cluster --cluster-name missing-person-cluster
   ```

2. **Build and Push Docker Images**
   ```bash
   # Build backend
   docker build -t your-ecr-repo/missing-person-backend:latest ./backend
   docker push your-ecr-repo/missing-person-backend:latest

   # Build frontend
   docker build -t your-ecr-repo/missing-person-frontend:latest ./frontend
   docker push your-ecr-repo/missing-person-frontend:latest
   ```

3. **Deploy with ECS**
   ```bash
   docker compose -f deployment/aws-deploy.yml up
   ```

### Azure Deployment

1. **Create Azure Container Instances**
   ```bash
   az container create --resource-group myResourceGroup \
     --name missing-person-app \
     --image your-acr.azurecr.io/missing-person:latest
   ```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue on GitHub
- Contact: support@missing-person-tracker.com

## 🙏 Acknowledgments

- OpenCV community for computer vision tools
- React and Spring Boot communities
- All contributors and testers

---

**Made with ❤️ to help reunite families and recover lost items**