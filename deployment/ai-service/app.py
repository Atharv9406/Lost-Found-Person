from flask import Flask, request, jsonify
from flask_cors import CORS
import cv2
import face_recognition
import numpy as np
import base64
import os
from PIL import Image
import io
import logging

app = Flask(__name__)
CORS(app)

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class AIService:
    def __init__(self):
        self.known_face_encodings = []
        self.known_face_names = []
        
    def encode_image_from_base64(self, base64_string):
        """Convert base64 string to face encoding"""
        try:
            # Remove data URL prefix if present
            if ',' in base64_string:
                base64_string = base64_string.split(',')[1]
            
            # Decode base64
            image_bytes = base64.b64decode(base64_string)
            image = Image.open(io.BytesIO(image_bytes))
            image_np = np.array(image)
            
            # Convert RGB to BGR for OpenCV
            if len(image_np.shape) == 3:
                image_np = cv2.cvtColor(image_np, cv2.COLOR_RGB2BGR)
            
            # Find face encodings
            face_encodings = face_recognition.face_encodings(image_np)
            
            if len(face_encodings) > 0:
                return face_encodings[0].tolist()
            else:
                return None
                
        except Exception as e:
            logger.error(f"Error encoding image: {str(e)}")
            return None
    
    def compare_faces(self, known_encoding, unknown_encoding, tolerance=0.6):
        """Compare two face encodings"""
        try:
            known_np = np.array(known_encoding)
            unknown_np = np.array(unknown_encoding)
            
            distance = np.linalg.norm(known_np - unknown_np)
            similarity = max(0, 100 - (distance * 100))
            
            return {
                'match': distance < tolerance,
                'similarity': similarity,
                'distance': distance
            }
        except Exception as e:
            logger.error(f"Error comparing faces: {str(e)}")
            return None
    
    def detect_objects(self, base64_string):
        """Basic object detection using OpenCV"""
        try:
            # Remove data URL prefix if present
            if ',' in base64_string:
                base64_string = base64_string.split(',')[1]
            
            # Decode base64
            image_bytes = base64.b64decode(base64_string)
            image = Image.open(io.BytesIO(image_bytes))
            image_np = np.array(image)
            
            # Convert RGB to BGR for OpenCV
            if len(image_np.shape) == 3:
                image_np = cv2.cvtColor(image_np, cv2.COLOR_RGB2BGR)
            
            # Simple object detection using contours
            gray = cv2.cvtColor(image_np, cv2.COLOR_BGR2GRAY)
            blur = cv2.GaussianBlur(gray, (5, 5), 0)
            thresh = cv2.threshold(blur, 60, 255, cv2.THRESH_BINARY)[1]
            
            contours, _ = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
            
            objects = []
            for contour in contours:
                area = cv2.contourArea(contour)
                if area > 1000:  # Filter small objects
                    x, y, w, h = cv2.boundingRect(contour)
                    objects.append({
                        'type': 'object',
                        'confidence': min(100, area / 10000 * 100),
                        'bbox': [x, y, w, h],
                        'area': area
                    })
            
            return objects[:10]  # Return top 10 objects
            
        except Exception as e:
            logger.error(f"Error detecting objects: {str(e)}")
            return []

ai_service = AIService()

@app.route('/api/face-recognition', methods=['POST'])
def face_recognition_endpoint():
    try:
        data = request.json
        image_data = data.get('image')
        
        if not image_data:
            return jsonify({'error': 'No image data provided'}), 400
        
        # Encode the face
        encoding = ai_service.encode_image_from_base64(image_data)
        
        if encoding is None:
            return jsonify({'error': 'No face detected in image'}), 400
        
        response = {
            'success': True,
            'face_encoding': encoding,
            'faces_detected': 1,
            'message': 'Face encoded successfully'
        }
        
        return jsonify(response)
        
    except Exception as e:
        logger.error(f"Face recognition error: {str(e)}")
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/api/compare-faces', methods=['POST'])
def compare_faces_endpoint():
    try:
        data = request.json
        encoding1 = data.get('encoding1')
        encoding2 = data.get('encoding2')
        tolerance = data.get('tolerance', 0.6)
        
        if not encoding1 or not encoding2:
            return jsonify({'error': 'Two face encodings required'}), 400
        
        result = ai_service.compare_faces(encoding1, encoding2, tolerance)
        
        if result is None:
            return jsonify({'error': 'Error comparing faces'}), 500
        
        return jsonify({
            'success': True,
            'match': result['match'],
            'similarity': result['similarity'],
            'distance': result['distance']
        })
        
    except Exception as e:
        logger.error(f"Face comparison error: {str(e)}")
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/api/object-detection', methods=['POST'])
def object_detection_endpoint():
    try:
        data = request.json
        image_data = data.get('image')
        
        if not image_data:
            return jsonify({'error': 'No image data provided'}), 400
        
        objects = ai_service.detect_objects(image_data)
        
        return jsonify({
            'success': True,
            'objects': objects,
            'count': len(objects)
        })
        
    except Exception as e:
        logger.error(f"Object detection error: {str(e)}")
        return jsonify({'error': 'Internal server error'}), 500

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy', 'service': 'AI Service'})

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=False)