export interface CustomerAccounts {
  type: string
  id: string
  createdAt: string
  balance: number
  status: string
  customerDTO: CustomerDTO
  interestRate: number
}

export interface CustomerDTO {
  id: number
  name: string
  email: string
}
