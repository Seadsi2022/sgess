jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EeventualiteService } from '../service/eeventualite.service';

import { EeventualiteDeleteDialogComponent } from './eeventualite-delete-dialog.component';

describe('Eeventualite Management Delete Component', () => {
  let comp: EeventualiteDeleteDialogComponent;
  let fixture: ComponentFixture<EeventualiteDeleteDialogComponent>;
  let service: EeventualiteService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EeventualiteDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(EeventualiteDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EeventualiteDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EeventualiteService);
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
