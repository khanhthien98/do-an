import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class BillService {
  constructor(private http: HttpBaseService) {}
  getBills(params): Observable<any> {
    return this.http.get<any>(`/api/v1/bills`, params);
  }
 
  getBillDetail(id): Observable<any> {
    return this.http.get<any>(`/api/v1/bill-detail/` + id, null);
  }
 
  saveBill(bill): Observable<any> {
    return this.http.post<any>(`/api/v1/bill`, bill);
  }
}
