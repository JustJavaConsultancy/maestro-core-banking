export interface IApprovalWorkflow {
  id?: number;
  name?: string;
  initiatorName?: string;
  initiatorId?: number;
  approverName?: string;
  approverId?: number;
  transactionTypeName?: string;
  transactionTypeId?: number;
}

export const defaultValue: Readonly<IApprovalWorkflow> = {};
