import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  CircularProgress,
  Alert,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { Report } from '../types';
import { reportService } from '../services/api';
import MapComponent from '../components/Map/MapComponent';

const HomePage: React.FC = () => {
  const [nearbyReports, setNearbyReports] = useState<Report[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [userLocation, setUserLocation] = useState<[number, number] | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const initializeLocation = () => {
      getUserLocation();
    };
    initializeLocation();
  }, []); // Empty dependency array is intentional for component mount

  const getUserLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          setUserLocation([latitude, longitude]);
          loadNearbyReports(latitude, longitude);
        },
        (error) => {
          console.error('Error getting location:', error);
          // Default to New York City if location access is denied
          setUserLocation([40.7128, -74.0060]);
          loadNearbyReports(40.7128, -74.0060);
        }
      );
    } else {
      setUserLocation([40.7128, -74.0060]);
      loadNearbyReports(40.7128, -74.0060);
    }
  };

  const loadNearbyReports = async (lat: number, lng: number) => {
    try {
      const reports = await reportService.getNearbyReports({
        latitude: lat,
        longitude: lng,
        radiusInMeters: 50000, // 50km radius
      });
      setNearbyReports(reports.slice(0, 6)); // Show only first 6 reports
    } catch (err) {
      setError('Failed to load nearby reports');
      console.error('Error loading nearby reports:', err);
    } finally {
      setLoading(false);
    }
  };

  const getReportTypeColor = (type: string) => {
    switch (type) {
      case 'MISSING_PERSON':
        return '#f44336';
      case 'LOST_ITEM':
        return '#ff9800';
      case 'FOUND_PERSON':
        return '#4caf50';
      case 'FOUND_ITEM':
        return '#2196f3';
      default:
        return '#757575';
    }
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Hero Section */}
      <Box textAlign="center" mb={6}>
        <Typography variant="h2" component="h1" gutterBottom>
          Missing Person & Lost Item Tracker
        </Typography>
        <Typography variant="h5" color="text.secondary" paragraph>
          Help find missing people and lost items using AI-powered matching and community support
        </Typography>
        <Box sx={{ mt: 4, display: 'flex', gap: 2, justifyContent: 'center' }}>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/create-report')}
          >
            Report Missing Person/Item
          </Button>
          <Button
            variant="outlined"
            size="large"
            onClick={() => navigate('/reports')}
          >
            Browse All Reports
          </Button>
        </Box>
      </Box>

      {/* Map Section */}
      <Box mb={6}>
        <Typography variant="h4" component="h2" gutterBottom>
          Nearby Reports
        </Typography>
        {userLocation ? (
          <MapComponent
            center={userLocation}
            reports={nearbyReports}
            height="500px"
          />
        ) : (
          <Box display="flex" justifyContent="center" alignItems="center" height="500px">
            <CircularProgress />
          </Box>
        )}
      </Box>

      {/* Recent Reports Section */}
      <Box mb={6}>
        <Typography variant="h4" component="h2" gutterBottom>
          Recent Reports
        </Typography>
        
        {loading ? (
          <Box display="flex" justifyContent="center" py={4}>
            <CircularProgress />
          </Box>
        ) : error ? (
          <Alert severity="error">{error}</Alert>
        ) : (
          <Box display="flex" flexWrap="wrap" gap={2}>
            {nearbyReports.map((report) => (
              <Card 
                key={report.id}
                sx={{ 
                  width: { xs: '100%', sm: 'calc(50% - 8px)', md: 'calc(33.333% - 16px)' },
                  borderLeft: `4px solid ${getReportTypeColor(report.type)}`,
                  cursor: 'pointer',
                  '&:hover': {
                    boxShadow: 3,
                  },
                }}
                onClick={() => navigate(`/reports/${report.id}`)}
              >
                  <CardContent>
                    <Typography 
                      variant="h6" 
                      component="h3" 
                      gutterBottom
                      sx={{ color: getReportTypeColor(report.type) }}
                    >
                      {report.title}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" gutterBottom>
                      {report.type.replace('_', ' ')} • {report.status}
                    </Typography>
                    <Typography variant="body2" paragraph>
                      {report.description.length > 100
                        ? `${report.description.substring(0, 100)}...`
                        : report.description}
                    </Typography>
                    <Typography variant="caption" color="text.secondary">
                      Reported: {new Date(report.createdAt).toLocaleDateString()}
                    </Typography>
                    {report.lastSeenLocation.address && (
                      <Typography variant="caption" display="block" color="text.secondary">
                        Location: {report.lastSeenLocation.address}
                      </Typography>
                    )}
                  </CardContent>
                </Card>
              ))}
            </Box>
        )}

        {!loading && nearbyReports.length === 0 && (
          <Typography variant="body1" textAlign="center" py={4}>
            No nearby reports found. Try browsing all reports or create a new one.
          </Typography>
        )}
      </Box>

      {/* Call to Action */}
      <Box textAlign="center" py={4} sx={{ backgroundColor: '#f5f5f5', borderRadius: 2 }}>
        <Typography variant="h5" gutterBottom>
          Help Someone Find Their Way Home
        </Typography>
        <Typography variant="body1" paragraph>
          Every report matters. Join our community in helping reunite families and recover lost items.
        </Typography>
        <Button
          variant="contained"
          size="large"
          onClick={() => navigate('/create-report')}
        >
          Submit a Report
        </Button>
      </Box>
    </Container>
  );
};

export default HomePage;