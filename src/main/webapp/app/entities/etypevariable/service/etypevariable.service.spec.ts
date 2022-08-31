import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEtypevariable } from '../etypevariable.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../etypevariable.test-samples';

import { EtypevariableService } from './etypevariable.service';

const requireRestSample: IEtypevariable = {
  ...sampleWithRequiredData,
};

describe('Etypevariable Service', () => {
  let service: EtypevariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IEtypevariable | IEtypevariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EtypevariableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Etypevariable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const etypevariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(etypevariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Etypevariable', () => {
      const etypevariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(etypevariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Etypevariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Etypevariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Etypevariable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEtypevariableToCollectionIfMissing', () => {
      it('should add a Etypevariable to an empty array', () => {
        const etypevariable: IEtypevariable = sampleWithRequiredData;
        expectedResult = service.addEtypevariableToCollectionIfMissing([], etypevariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etypevariable);
      });

      it('should not add a Etypevariable to an array that contains it', () => {
        const etypevariable: IEtypevariable = sampleWithRequiredData;
        const etypevariableCollection: IEtypevariable[] = [
          {
            ...etypevariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEtypevariableToCollectionIfMissing(etypevariableCollection, etypevariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Etypevariable to an array that doesn't contain it", () => {
        const etypevariable: IEtypevariable = sampleWithRequiredData;
        const etypevariableCollection: IEtypevariable[] = [sampleWithPartialData];
        expectedResult = service.addEtypevariableToCollectionIfMissing(etypevariableCollection, etypevariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etypevariable);
      });

      it('should add only unique Etypevariable to an array', () => {
        const etypevariableArray: IEtypevariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const etypevariableCollection: IEtypevariable[] = [sampleWithRequiredData];
        expectedResult = service.addEtypevariableToCollectionIfMissing(etypevariableCollection, ...etypevariableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const etypevariable: IEtypevariable = sampleWithRequiredData;
        const etypevariable2: IEtypevariable = sampleWithPartialData;
        expectedResult = service.addEtypevariableToCollectionIfMissing([], etypevariable, etypevariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etypevariable);
        expect(expectedResult).toContain(etypevariable2);
      });

      it('should accept null and undefined values', () => {
        const etypevariable: IEtypevariable = sampleWithRequiredData;
        expectedResult = service.addEtypevariableToCollectionIfMissing([], null, etypevariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etypevariable);
      });

      it('should return initial array if no Etypevariable is added', () => {
        const etypevariableCollection: IEtypevariable[] = [sampleWithRequiredData];
        expectedResult = service.addEtypevariableToCollectionIfMissing(etypevariableCollection, undefined, null);
        expect(expectedResult).toEqual(etypevariableCollection);
      });
    });

    describe('compareEtypevariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEtypevariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEtypevariable(entity1, entity2);
        const compareResult2 = service.compareEtypevariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEtypevariable(entity1, entity2);
        const compareResult2 = service.compareEtypevariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEtypevariable(entity1, entity2);
        const compareResult2 = service.compareEtypevariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
