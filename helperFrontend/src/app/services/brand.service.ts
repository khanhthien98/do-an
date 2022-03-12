import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class BrandService {
  constructor(private http: HttpBaseService) {}
  getBrands(params: any): Observable<any> {
    return this.http.get<any>(`/api/v1/brands`, params);
  }
  getAllBrand(): Observable<any> {
    return this.http.get<any>(`/api/v1/brands/all`, null);
  }
 
}
