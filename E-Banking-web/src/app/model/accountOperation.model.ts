export interface AccountDetails {
  accountId:            string;
  balance:              number;
  accountOperationDTOS: AccountOperation[];
  currentPage:          number;
  pageSize:             number;
  totalPages:           number;
}
export interface AccountOperation {
  id:          number;
  date:        Date;
  amount:      number;
  type:        string;
  description: string;
}
