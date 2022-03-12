import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class BillService {
  constructor(private http: HttpBaseService) {}
  getBills(params): Observable<any> {
    return this.http.get<any>(`/api/admin/v1/bills`, params);
  }
  exportBills(params): Observable<any> {
    return this.http.exportExcel<any>(`/api/admin/v1/export-bills`, params);
  }
  getBillDetail(id): Observable<any> {
    return this.http.get<any>(`/api/admin/v1/bill-detail/` + id, null);
  }
  deleteBill(id): Observable<any> {
    return this.http.delete<any>(`/api/admin/v1/bill/` + id);
  }
  cancelBill(bill): Observable<any> {
    return this.http.post<any>(`/api/admin/v1/cancel-bill`, bill);
  }
  saveBill(bill): Observable<any> {
    return this.http.post<any>(`/api/admin/v1/bill`, bill);
  }
}
