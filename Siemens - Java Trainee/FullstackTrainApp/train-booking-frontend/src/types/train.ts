export interface TrainResponse {
  id: number;
  code: string;
  name: string;
  capacity: number;
  active: boolean;
}

export interface TrainRequest {
  code: string;
  name: string;
  capacity: number;
}