import { Brand } from './Brand.model';
import { Category } from './Category.model';

export class Product {
  productID: number;
  productName: string;
  productImage: string;
  category: Category;
  brand: Brand;
  productDescription: string;
  productPrice: number;
  productImportPrice: number;
  productQuantily: number;
  productDimensions: string;
  productWeight: number;
  productMaterial: string;
}
