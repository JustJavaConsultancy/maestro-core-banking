import { InsuranceOperation } from 'app/shared/model/enumerations/insurance-operation.model';

export interface IVehicleInsuranceRequest {
  id?: number;
  operation?: InsuranceOperation;
  policyNo?: string;
  certificateNo?: string;
  occupation?: string;
  sector?: string;
  idType?: string;
  idNumber?: string;
  vehicleType?: string;
  registrationNo?: string;
  vehMake?: string;
  vehModel?: string;
  vehYear?: number;
  registrationStart?: string;
  expiryDate?: string;
  mileage?: string;
  vehColor?: string;
  chassisNo?: string;
  engineNo?: string;
  engineCapacity?: string;
  seatCapacity?: string;
  balance?: number;
  profileId?: number;
  insuranceTypeId?: number;
}

export const defaultValue: Readonly<IVehicleInsuranceRequest> = {};
