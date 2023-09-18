import { IProfileMySuffix } from 'app/shared/model/profile-my-suffix.model';

export interface IApprovalGroup {
  id?: number;
  name?: string;
  profiles?: IProfileMySuffix[];
}

export const defaultValue: Readonly<IApprovalGroup> = {};
