import React from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from 'react-leaflet';
import { Icon } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { Report } from '../../types';

// Fix for default markers
delete (Icon.Default.prototype as any)._getIconUrl;
Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

interface MapComponentProps {
  center: [number, number];
  zoom?: number;
  reports?: Report[];
  onLocationSelect?: (lat: number, lng: number) => void;
  clickable?: boolean;
  height?: string;
}

const MapEvents: React.FC<{ onLocationSelect?: (lat: number, lng: number) => void }> = ({ onLocationSelect }) => {
  useMapEvents({
    click: (e) => {
      if (onLocationSelect) {
        const { lat, lng } = e.latlng;
        onLocationSelect(lat, lng);
      }
    },
  });
  return null;
};

const MapComponent: React.FC<MapComponentProps> = ({
  center,
  zoom = 13,
  reports = [],
  onLocationSelect,
  clickable = false,
  height = '400px',
}) => {
  const getMarkerColor = (report: Report) => {
    // Function to get marker color based on report type
    // Currently using default markers, but this could be extended
    switch (report.type) {
      case 'MISSING_PERSON':
        return 'red';
      case 'LOST_ITEM':
        return 'orange';
      case 'FOUND_PERSON':
        return 'green';
      case 'FOUND_ITEM':
        return 'blue';
      default:
        return 'gray';
    }
  };

  // Suppress unused variable warning for future use
  console.log('Map component loaded with', reports.length, 'reports');
  if (reports.length > 0) {
    console.log('First report marker color would be:', getMarkerColor(reports[0]));
  }

  return (
    <MapContainer
      center={center}
      zoom={zoom}
      style={{ height, width: '100%' }}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      
      {clickable && <MapEvents onLocationSelect={onLocationSelect} />}
      
      {reports.map((report) => (
        <Marker
          key={report.id}
          position={[report.lastSeenLocation.latitude, report.lastSeenLocation.longitude]}
        >
          <Popup>
            <div>
              <h3>{report.title}</h3>
              <p><strong>Type:</strong> {report.type.replace('_', ' ')}</p>
              <p><strong>Status:</strong> {report.status}</p>
              <p><strong>Description:</strong> {report.description}</p>
              {report.lastSeenLocation.address && (
                <p><strong>Location:</strong> {report.lastSeenLocation.address}</p>
              )}
              <p><strong>Reported:</strong> {new Date(report.createdAt).toLocaleDateString()}</p>
              {report.contactPhone && (
                <p><strong>Contact:</strong> {report.contactPhone}</p>
              )}
            </div>
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};

export default MapComponent;