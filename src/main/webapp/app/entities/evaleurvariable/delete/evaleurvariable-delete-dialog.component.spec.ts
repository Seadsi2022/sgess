jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EvaleurvariableService } from '../service/evaleurvariable.service';

import { EvaleurvariableDeleteDialogComponent } from './evaleurvariable-delete-dialog.component';

describe('Evaleurvariable Management Delete Component', () => {
  let comp: EvaleurvariableDeleteDialogComponent;
  let fixture: ComponentFixture<EvaleurvariableDeleteDialogComponent>;
  let service: EvaleurvariableService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EvaleurvariableDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(EvaleurvariableDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvaleurvariableDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EvaleurvariableService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
