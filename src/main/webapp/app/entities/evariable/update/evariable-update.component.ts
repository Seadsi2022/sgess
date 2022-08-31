import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EvariableFormService, EvariableFormGroup } from './evariable-form.service';
import { IEvariable } from '../evariable.model';
import { EvariableService } from '../service/evariable.service';
import { IEtypevariable } from 'app/entities/etypevariable/etypevariable.model';
import { EtypevariableService } from 'app/entities/etypevariable/service/etypevariable.service';
import { IEunite } from 'app/entities/eunite/eunite.model';
import { EuniteService } from 'app/entities/eunite/service/eunite.service';

@Component({
  selector: 'jhi-evariable-update',
  templateUrl: './evariable-update.component.html',
})
export class EvariableUpdateComponent implements OnInit {
  isSaving = false;
  evariable: IEvariable | null = null;

  etypevariablesSharedCollection: IEtypevariable[] = [];
  eunitesSharedCollection: IEunite[] = [];

  editForm: EvariableFormGroup = this.evariableFormService.createEvariableFormGroup();

  constructor(
    protected evariableService: EvariableService,
    protected evariableFormService: EvariableFormService,
    protected etypevariableService: EtypevariableService,
    protected euniteService: EuniteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEtypevariable = (o1: IEtypevariable | null, o2: IEtypevariable | null): boolean =>
    this.etypevariableService.compareEtypevariable(o1, o2);

  compareEunite = (o1: IEunite | null, o2: IEunite | null): boolean => this.euniteService.compareEunite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evariable }) => {
      this.evariable = evariable;
      if (evariable) {
        this.updateForm(evariable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evariable = this.evariableFormService.getEvariable(this.editForm);
    if (evariable.id !== null) {
      this.subscribeToSaveResponse(this.evariableService.update(evariable));
    } else {
      this.subscribeToSaveResponse(this.evariableService.create(evariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvariable>>): void {
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

  protected updateForm(evariable: IEvariable): void {
    this.evariable = evariable;
    this.evariableFormService.resetForm(this.editForm, evariable);

    this.etypevariablesSharedCollection = this.etypevariableService.addEtypevariableToCollectionIfMissing<IEtypevariable>(
      this.etypevariablesSharedCollection,
      evariable.etypevariable
    );
    this.eunitesSharedCollection = this.euniteService.addEuniteToCollectionIfMissing<IEunite>(
      this.eunitesSharedCollection,
      evariable.eunite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etypevariableService
      .query()
      .pipe(map((res: HttpResponse<IEtypevariable[]>) => res.body ?? []))
      .pipe(
        map((etypevariables: IEtypevariable[]) =>
          this.etypevariableService.addEtypevariableToCollectionIfMissing<IEtypevariable>(etypevariables, this.evariable?.etypevariable)
        )
      )
      .subscribe((etypevariables: IEtypevariable[]) => (this.etypevariablesSharedCollection = etypevariables));

    this.euniteService
      .query()
      .pipe(map((res: HttpResponse<IEunite[]>) => res.body ?? []))
      .pipe(map((eunites: IEunite[]) => this.euniteService.addEuniteToCollectionIfMissing<IEunite>(eunites, this.evariable?.eunite)))
      .subscribe((eunites: IEunite[]) => (this.eunitesSharedCollection = eunites));
  }
}
