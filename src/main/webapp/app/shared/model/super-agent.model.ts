import { AgentStatus } from 'app/shared/model/enumerations/agent-status.model';

export interface ISuperAgent {
  id?: number;
  status?: AgentStatus;
  agentId?: number;
  schemeSchemeID?: string;
  schemeId?: number;
}

export const defaultValue: Readonly<ISuperAgent> = {};
