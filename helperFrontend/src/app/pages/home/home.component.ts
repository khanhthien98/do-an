import { Component, OnInit } from '@angular/core';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Product } from 'src/app/entity/Product.model';
import { BrandService } from 'src/app/services/brand.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  listOfData: Product[] = [];
  pageSize = 9;
  pageIndex = 1;
  product:Product = null;
  controlArray: Map<string, any> = new Map<string, any>();
  totalPrice:number = 0;
  cart: Product[] = [];
  constructor(
    private categoryService: CategoryService,
    private brandService: BrandService,
    private productService: ProductService,
    private notification: NzNotificationService
  ) {}

  ngOnInit(): void {
     // declare cart 
     const cart = localStorage.getItem('cart') || '';
     if (cart) {
       this.cart = JSON.parse(cart);
     }

    this.getProducts(this.pageIndex,this.pageSize,'productID','descend')
  }
  createNotification(type: string, title: string, message: string): void {
    this.notification.create(type, title, message);
  }
  getProducts(
    pageIndex: number,
    pageSize: number,
    sortField: string | null,
    sortOrder: string | null
  ) {
    this.controlArray.set('pageIndex', pageIndex);
    this.controlArray.set('pageSize', pageSize);
    this.controlArray.set('sortField', sortField);
    this.controlArray.set('sortOrder', sortOrder);
    // get product
    this.productService.getProducts(this.controlArray).subscribe(
      (data) => {
        if (data && data.results) {
          this.listOfData = data.results;
          this.product = this.listOfData[0];
        }
      },
      (error) => {
        this.createNotification(
          'error',
          'Có lỗi xảy ra!',
          'Vui lòng liên hệ quản trị viên.'
        );
      }
    );
  }

  addToCart(product:Product){
    let duplicate = false;
    this.cart.forEach((ele) => {
      if (ele.productID == product.productID ) {
        duplicate = true;
      }
    });
    // if no => add item
    if(!duplicate){
      product.quanlityBuy = 1;
      this.cart.push(product);
      this.updateCart();
      this.createNotification(
        'success',
        'Sản phẩm đã thêm vào giỏ hàng',
        ''
      );
    }else{
      this.createNotification(
        'info',
        'Sản phẩm đã có trong giỏ hàng',
        ''
      );
    }
  }
  updateCart() {
    this.totalPrice = 0;
    this.cart.forEach((ele) => {
      this.totalPrice += ele.quanlityBuy * ele.productPrice;
    });
    localStorage.setItem('total', this.totalPrice.toString());
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }
}
