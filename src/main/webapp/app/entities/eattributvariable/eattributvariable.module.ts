import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EattributvariableComponent } from './list/eattributvariable.component';
import { EattributvariableDetailComponent } from './detail/eattributvariable-detail.component';
import { EattributvariableUpdateComponent } from './update/eattributvariable-update.component';
import { EattributvariableDeleteDialogComponent } from './delete/eattributvariable-delete-dialog.component';
import { EattributvariableRoutingModule } from './route/eattributvariable-routing.module';

@NgModule({
  imports: [SharedModule, EattributvariableRoutingModule],
  declarations: [
    EattributvariableComponent,
    EattributvariableDetailComponent,
    EattributvariableUpdateComponent,
    EattributvariableDeleteDialogComponent,
  ],
})
export class EattributvariableModule {}
