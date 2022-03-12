import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpBaseService } from './http-base.service';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  constructor(private http: HttpBaseService) {}
  getProducts(params): Observable<any> {
    return this.http.get<any>(`/api/v1/products`, params);
  }
  getProduct(id): Observable<any> {
    return this.http.get<any>(`/api/v1/product/` + id, null);
  }
}
