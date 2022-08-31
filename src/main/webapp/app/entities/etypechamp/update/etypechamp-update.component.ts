import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EtypechampFormService, EtypechampFormGroup } from './etypechamp-form.service';
import { IEtypechamp } from '../etypechamp.model';
import { EtypechampService } from '../service/etypechamp.service';
import { IEattribut } from 'app/entities/eattribut/eattribut.model';
import { EattributService } from 'app/entities/eattribut/service/eattribut.service';

@Component({
  selector: 'jhi-etypechamp-update',
  templateUrl: './etypechamp-update.component.html',
})
export class EtypechampUpdateComponent implements OnInit {
  isSaving = false;
  etypechamp: IEtypechamp | null = null;

  eattributsSharedCollection: IEattribut[] = [];

  editForm: EtypechampFormGroup = this.etypechampFormService.createEtypechampFormGroup();

  constructor(
    protected etypechampService: EtypechampService,
    protected etypechampFormService: EtypechampFormService,
    protected eattributService: EattributService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEattribut = (o1: IEattribut | null, o2: IEattribut | null): boolean => this.eattributService.compareEattribut(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etypechamp }) => {
      this.etypechamp = etypechamp;
      if (etypechamp) {
        this.updateForm(etypechamp);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etypechamp = this.etypechampFormService.getEtypechamp(this.editForm);
    if (etypechamp.id !== null) {
      this.subscribeToSaveResponse(this.etypechampService.update(etypechamp));
    } else {
      this.subscribeToSaveResponse(this.etypechampService.create(etypechamp));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtypechamp>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(etypechamp: IEtypechamp): void {
    this.etypechamp = etypechamp;
    this.etypechampFormService.resetForm(this.editForm, etypechamp);

    this.eattributsSharedCollection = this.eattributService.addEattributToCollectionIfMissing<IEattribut>(
      this.eattributsSharedCollection,
      ...(etypechamp.eattributs ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eattributService
      .query()
      .pipe(map((res: HttpResponse<IEattribut[]>) => res.body ?? []))
      .pipe(
        map((eattributs: IEattribut[]) =>
          this.eattributService.addEattributToCollectionIfMissing<IEattribut>(eattributs, ...(this.etypechamp?.eattributs ?? []))
        )
      )
      .subscribe((eattributs: IEattribut[]) => (this.eattributsSharedCollection = eattributs));
  }
}
