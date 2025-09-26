export interface User {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  roles: Role[];
}

export enum Role {
  USER = 'USER',
  ADMIN = 'ADMIN',
  AUTHORITY = 'AUTHORITY'
}

export interface AuthResponse {
  token: string;
  type: string;
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  roles: Role[];
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
}

export enum ReportType {
  MISSING_PERSON = 'MISSING_PERSON',
  LOST_ITEM = 'LOST_ITEM',
  FOUND_PERSON = 'FOUND_PERSON',
  FOUND_ITEM = 'FOUND_ITEM'
}

export enum ReportStatus {
  ACTIVE = 'ACTIVE',
  RESOLVED = 'RESOLVED',
  CANCELLED = 'CANCELLED',
  EXPIRED = 'EXPIRED'
}

export interface Location {
  latitude: number;
  longitude: number;
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  postalCode?: string;
}

export interface PersonDetails {
  fullName: string;
  age?: number;
  gender?: string;
  height?: number;
  weight?: number;
  hairColor?: string;
  eyeColor?: string;
  complexion?: string;
  clothingDescription?: string;
  distinguishingMarks?: string;
  medicalConditions?: string[];
  emergencyContactName?: string;
  emergencyContactPhone?: string;
}

export interface ItemDetails {
  itemName: string;
  category?: string;
  brand?: string;
  model?: string;
  color?: string;
  size?: string;
  serialNumber?: string;
  description?: string;
  estimatedValue?: number;
}

export interface Report {
  id: string;
  type: ReportType;
  title: string;
  description: string;
  reporter: User;
  personDetails?: PersonDetails;
  itemDetails?: ItemDetails;
  lastSeenLocation: Location;
  status: ReportStatus;
  imageUrls: string[];
  contactPhone?: string;
  contactEmail?: string;
  rewardAmount?: number;
  isPublic: boolean;
  createdAt: string;
  updatedAt: string;
  incidentDateTime?: string;
}

export interface CreateReportRequest {
  type: ReportType;
  title: string;
  description: string;
  personDetails?: PersonDetails;
  itemDetails?: ItemDetails;
  lastSeenLocation: Location;
  imageUrls?: string[];
  contactPhone?: string;
  contactEmail?: string;
  rewardAmount?: number;
  isPublic?: boolean;
  incidentDateTime?: string;
}